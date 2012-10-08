delete from kabouterserver.devicetemplate;

insert into kabouterserver.devicetemplate (numio, ionames, iotypes, iodirections, templatename) 
    values ('2', '{"temp_in","relay_out"}', '{"analog","digital"}', '{"input","output"}', 'KABOUTER_TEST_DEVICE');

insert into kabouterserver.devicetemplate (numio, ionames, iotypes, iodirections, templatename) 
    values ('2', '{"temp_in","relay_out"}', '{"analog","digital"}', '{"input","output"}', 'ARDUINO_POWER');