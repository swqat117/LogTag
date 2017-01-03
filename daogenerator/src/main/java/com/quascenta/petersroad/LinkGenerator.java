package com.quascenta.petersroad;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;
import de.greenrobot.daogenerator.ToOne;

public class LinkGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.quascenta.petersroad.broadway.model");

        // 28/9/16 Entering the two schemas device and sensors
        // each device has n sensors and determining no of sensors depends on chnact from config
        //Each sensor has an alm high and a low in its property

        Entity device = schema.addEntity("device");
        Property device_id = device.addIdProperty().primaryKey().autoincrement().columnName("id").getProperty();

        device.addStringProperty("device_name");
        device.addFloatProperty("device_interval").notNull();
        Property device_chn = device.addIntProperty("DEVICE_CHNACT").getProperty();
        device.addStringProperty("device_description");
        device.addLongProperty("device_tzone");
        device.addStringProperty("device_loggerid");
        device.addStringProperty("device_ip");


        Entity sensor = schema.addEntity("sensors");
        Property sens_id = sensor.addIdProperty().primaryKey().columnName("sensor_id").getProperty();
        sensor.addStringProperty("sensor_name");
        sensor.addStringProperty("sensor_status");
        sensor.addStringProperty("sensor_alarmhigh");
        sensor.addStringProperty("sensor_alarmlow");
        sensor.addStringProperty("sensor_description");


        Entity sensorLOG = schema.addEntity("slog");
        Property sensorLog_id = sensorLOG.addIdProperty().primaryKey().columnName("slog_id").getProperty();
        sensorLOG.addLongProperty("sl_time");
        sensorLOG.addFloatProperty("sl_value");
        sensorLOG.addBooleanProperty("sl_active");

        Entity unit = schema.addEntity("unit");
        Property unit_id = unit.addIdProperty().primaryKey().columnName("unit_id").getProperty();
        unit.addStringProperty("type");
        unit.addStringProperty("value");

        Entity SampleDevice = schema.addEntity("device_log");
        SampleDevice.addStringProperty("data_id");
        SampleDevice.addStringProperty("config_id");
        SampleDevice.addStringProperty("datetime");
        SampleDevice.addStringProperty("sen1");
        SampleDevice.addStringProperty("sen2");
        SampleDevice.addStringProperty("sen3");
        SampleDevice.addStringProperty("sen4");
        SampleDevice.addStringProperty("sen5");
        SampleDevice.addStringProperty("sen6");
        SampleDevice.addStringProperty("sen7");
        SampleDevice.addStringProperty("sen8");


        Entity user = schema.addEntity("user");
        user.addStringProperty("user_name");
        user.addStringProperty("user_email");
        user.addStringProperty("user_password").notNull();
        user.addStringProperty("user_mobile_no").unique();
        Property userid = user.addIdProperty().columnName("user_id").getProperty();


        ToMany UsertoDevice = user.addToMany(device, userid);
        ToMany devicetosensors = device.addToMany(sensor, device_id);
        ToOne sensortosensorLog = sensor.addToOne(sensorLOG, sens_id);
        ToOne sensorLogtoUnit = sensorLOG.addToOne(unit, sensorLog_id);


        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }
}