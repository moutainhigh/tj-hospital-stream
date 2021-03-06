-- 单台开关柜季度电度量状况
CREATE TABLE MAXIMO.RE_VOLTAGE_EM_QUARTER
(
    REVOLTAGEEMQUARTERID   BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1 ),
    IEDNAME                VARGRAPHIC(100 CODEUNITS16),
    ASSETYPE               VARGRAPHIC(100 CODEUNITS16),
    PRODUCTMODEL           VARGRAPHIC(100 CODEUNITS16),
    LOCATION               VARGRAPHIC(100 CODEUNITS16),
    PRODUCTMODELC          VARGRAPHIC(100 CODEUNITS16),
    ActiveElectricDegree   DECIMAL(31, 4),
    ReactiveElectricDegree DECIMAL(31, 4),
    ColTime                VARGRAPHIC(50 CODEUNITS16),
    TIMEPOINT              VARGRAPHIC(50 CODEUNITS16),
    CREATETIME             VARGRAPHIC(50 CODEUNITS16),
    ElectricDegree         DECIMAL(31, 4),
    PRIMARY KEY (REVOLTAGEEMQUARTERID)
);


-- 单台开关柜年度电度量状况
CREATE TABLE MAXIMO.RE_VOLTAGE_EM_YEAR
(
    REVOLTAGEEMYEARID      BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1 ),
    IEDNAME                VARGRAPHIC(100 CODEUNITS16),
    ASSETYPE               VARGRAPHIC(100 CODEUNITS16),
    PRODUCTMODEL           VARGRAPHIC(100 CODEUNITS16),
    LOCATION               VARGRAPHIC(100 CODEUNITS16),
    PRODUCTMODELC          VARGRAPHIC(100 CODEUNITS16),
    ActiveElectricDegree   DECIMAL(31, 4),
    ReactiveElectricDegree DECIMAL(31, 4),
    ColTime                VARGRAPHIC(50 CODEUNITS16),
    TIMEPOINT              VARGRAPHIC(50 CODEUNITS16),
    CREATETIME             VARGRAPHIC(50 CODEUNITS16),
    ElectricDegree         DECIMAL(31, 4),
    PRIMARY KEY (REVOLTAGEEMYEARID)
);


-- 单台中压设备-季度总电度量比对
CREATE TABLE MAXIMO.RE_VOLTAGE_EM_COMP_QUARTER
(
    REVOLTAGEEMCOMPQUARTERID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1 ),
    IEDNAME                  VARGRAPHIC(100 CODEUNITS16),
    ASSETYPE                 VARGRAPHIC(100 CODEUNITS16),
    PRODUCTMODEL             VARGRAPHIC(100 CODEUNITS16),
    LOCATION                 VARGRAPHIC(100 CODEUNITS16),
    PRODUCTMODELC            VARGRAPHIC(100 CODEUNITS16),
    ELECTRICDEGREE           DECIMAL(31, 4),
    CREATETIME               VARGRAPHIC(100 CODEUNITS16),
    COMPARISON               VARGRAPHIC(50 CODEUNITS16),
    RATIO                    DECIMAL(8, 4),
    PRIMARY KEY (REVOLTAGEEMCOMPQUARTERID)
);

-- 单台中压设备-季度总电度量比对
CREATE TABLE MAXIMO.RE_VOLTAGE_EM_COMP_YEAR
(
    REVOLTAGEEMCOMPYEARID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1 ),
    IEDNAME               VARGRAPHIC(100 CODEUNITS16),
    ASSETYPE              VARGRAPHIC(100 CODEUNITS16),
    PRODUCTMODEL          VARGRAPHIC(100 CODEUNITS16),
    LOCATION              VARGRAPHIC(100 CODEUNITS16),
    PRODUCTMODELC         VARGRAPHIC(100 CODEUNITS16),
    ELECTRICDEGREE        DECIMAL(31, 4),
    CREATETIME            VARGRAPHIC(100 CODEUNITS16),
    COMPARISON            VARGRAPHIC(50 CODEUNITS16),
    RATIO                 DECIMAL(8, 4),
    PRIMARY KEY (REVOLTAGEEMCOMPYEARID)
);


comment on table RE_VOLTAGE_EM_COMP_QUARTER is '中压设备-季度总电度量比对';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.REVOLTAGEEMCOMPQUARTERID is '主键';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.IEDNAME is '设备编码';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.ASSETYPE is '设备类别';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.PRODUCTMODEL is '设备型号';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.LOCATION is '位置';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.PRODUCTMODELC is '段信息';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.ELECTRICDEGREE is '总电度量';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.CREATETIME is '时间-季度';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.COMPARISON is '对比结果';

comment on column RE_VOLTAGE_EM_COMP_QUARTER.RATIO is '变化率';