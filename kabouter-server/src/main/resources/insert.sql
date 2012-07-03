delete from kabouterserver.devicetemplate;

insert into kabouterserver.devicetemplate (numio, ionames, iotypes, iodirections, templatename) 
    values ('2', '{"test_io1","test_io2"}', '{"digital","analog"}', '{"output","input"}', 'KABOUTER_TEST_DEVICE');

insert into kabouterserver.devicetemplate (numio, ionames, iotypes, iodirections, templatename) 
    values ('18',
            '{"D1","D2","D3","D4","D5","D6","D7","D8","D9","D10","D11","D12","D13","A0","A1","A2","A3","A4","A5"}', 
            '{"digital","digital","digital","digital","digital","digital","digital","digital","digital","digital","digital","digital","digital","analog","analog","analog","analog","analog","analog"}', 
            '{"output","output","output","output","output","output","output","output","output","output","output","output","output","input","input","input","input","input","input"}', 
            'ARDUINO');