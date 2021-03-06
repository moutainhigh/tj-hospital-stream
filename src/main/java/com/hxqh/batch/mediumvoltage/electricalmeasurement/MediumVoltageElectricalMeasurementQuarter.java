package com.hxqh.batch.mediumvoltage.electricalmeasurement;

import com.hxqh.domain.info.DataStartEnd;
import com.hxqh.utils.ElasticSearchUtils;
import com.hxqh.utils.LevelUtils;
import com.hxqh.utils.RemindDateUtils;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.types.Row;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static com.hxqh.constant.Constant.*;

/**
 * 单台中压设备-季度有功电度量和无功电度量
 * <p>
 * Created by Ocean lin on 2020/3/25.
 *
 * @author Ocean lin
 */
public class MediumVoltageElectricalMeasurementQuarter {

    public static void main(String[] args) throws Exception {
        final int[] type = getType();

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        List<Tuple2<String, Row>> list = new ArrayList<>();
        List<Tuple2<String, Row>> endList = new ArrayList<>();
        Connection connection = ElasticSearchUtils.getConnection();
        DataStartEnd startEnd = RemindDateUtils.getLastMonthStartEndTime();
        String start = startEnd.getStart();
        String end = startEnd.getEnd();

        // String sqlFirst = "select IEDName,assetYpe,productModel,location,productModelC,ActiveElectricDegree,ReactiveElectricDegree,ColTime,CreateTime from yc_mediumvoltage3 where  ColTime>'2020-03-01 00:00:00' order by CreateTime asc limit 1";
        // String sqlEnd = "select IEDName,assetYpe,productModel,location,productModelC,ActiveElectricDegree,ReactiveElectricDegree,ColTime,CreateTime from yc_mediumvoltage3 where  ColTime<'2020-03-31 23:59:59' order by CreateTime desc limit 1";

        String sqlFirst = "select IEDName,assetYpe,productModel,location,productModelC,ActiveElectricDegree,ReactiveElectricDegree,ColTime,CreateTime from yc_mediumvoltage3 where  ColTime>'" + start + "' order by ColTime asc limit 1";
        String sqlEnd = "select IEDName,assetYpe,productModel,location,productModelC,ActiveElectricDegree,ReactiveElectricDegree,ColTime,CreateTime from yc_mediumvoltage3 where ColTime<'" + end + "' order by ColTime desc limit 1";
        System.out.println(sqlFirst);
        System.out.println(sqlEnd);

        PreparedStatement ps = connection.prepareStatement(sqlFirst);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            Row row = new Row(9);
            String iedName = resultSet.getString("IEDName");
            row.setField(0, iedName);
            row.setField(1, resultSet.getString("assetYpe"));
            row.setField(2, resultSet.getString("productModel"));
            row.setField(3, resultSet.getString("location"));
            row.setField(4, resultSet.getString("productModelC"));
            row.setField(5, resultSet.getDouble("ActiveElectricDegree"));
            row.setField(6, resultSet.getDouble("ReactiveElectricDegree"));
            row.setField(7, resultSet.getString("ColTime"));
            row.setField(8, resultSet.getString("CreateTime"));
            list.add(Tuple2.of(iedName, row));
        }
        DataSource<Tuple2<String, Row>> startDataSet = env.fromCollection(list);


        PreparedStatement psEnd = connection.prepareStatement(sqlEnd);
        ResultSet resultEnd = psEnd.executeQuery();
        while (resultEnd.next()) {
            Row row = new Row(13);
            String iedName = resultEnd.getString("IEDName");
            row.setField(0, iedName);
            row.setField(1, resultEnd.getString("assetYpe"));
            row.setField(2, resultEnd.getString("productModel"));
            row.setField(3, resultEnd.getString("location"));
            row.setField(4, resultEnd.getString("productModelC"));
            row.setField(5, resultEnd.getDouble("ActiveElectricDegree"));
            row.setField(6, resultEnd.getDouble("ReactiveElectricDegree"));
            row.setField(7, resultEnd.getString("ColTime"));
            row.setField(8, resultEnd.getString("CreateTime"));
            row.setField(9, RemindDateUtils.getLastQuarter());
            row.setField(10, 0.0d);
            row.setField(11, 0.0d);
            row.setField(12, "");
            endList.add(Tuple2.of(iedName, row));
        }
        DataSource<Tuple2<String, Row>> endDataSet = env.fromCollection(endList);
        ElasticSearchUtils.close(connection, ps, resultSet);
        ElasticSearchUtils.closeResultSet(resultEnd);

        DataSet<Row> join = startDataSet.join(endDataSet).where(0).equalTo(0).with(new JoinFunction<Tuple2<String, Row>, Tuple2<String, Row>, Row>() {
            @Override
            public Row join(Tuple2<String, Row> first, Tuple2<String, Row> second) throws Exception {
                Double activeElectricDegree = Double.parseDouble(second.f1.getField(5).toString()) - Double.parseDouble(first.f1.getField(5).toString());
                Double reactiveElectricDegree = Double.parseDouble(second.f1.getField(6).toString()) - Double.parseDouble(first.f1.getField(6).toString());
                Double electricDegree = activeElectricDegree + reactiveElectricDegree;
                Double reactivePercent = reactiveElectricDegree / (electricDegree + 0.01);
                second.f1.setField(5, activeElectricDegree);
                second.f1.setField(6, reactiveElectricDegree);
                second.f1.setField(9, RemindDateUtils.getLastQuarter());
                second.f1.setField(10, electricDegree);
                second.f1.setField(11, reactivePercent);
                second.f1.setField(12, LevelUtils.computePercentageRreactive(reactivePercent));
                return second.f1;
            }
        });

        String insertQuery = "INSERT INTO RE_VOLTAGE_EM_QUARTER(IEDName,ASSETYPE,PRODUCTMODEL,LOCATION,productModelC,ActiveElectricDegree,ReactiveElectricDegree,ColTime,TIMEPOINT,CREATETIME,ElectricDegree,ReactivePercent,Opinion) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        // ElectricalMeasurement
        JDBCOutputFormat.JDBCOutputFormatBuilder outputBuilder =
                JDBCOutputFormat.buildJDBCOutputFormat().setDrivername(DB2_DRIVER_NAME).setDBUrl(DB2_DB_URL)
                        .setQuery(insertQuery).setSqlTypes(type).setUsername(DB2_USERNAME).setPassword(DB2_PASSWORD);
        join.output(outputBuilder.finish());

        env.execute("MediumVoltageElectricalMeasurementQuarter");

    }


    private static int[] getType() {
        return new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.DOUBLE,
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.DOUBLE, Types.VARCHAR};
    }
}
