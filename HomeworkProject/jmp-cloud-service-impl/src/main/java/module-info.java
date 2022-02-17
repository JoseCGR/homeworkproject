/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/module-info.java to edit this template
 */

module jmp.cloud.service.impl {
    requires transitive jmp.service.api;
    requires jmp.dto;
    requires java.sql;
            
    provides jmp.service.api.Service with jmp.cloud.service.impl.ServiceImplementation;
    
    exports jmp.cloud.service.impl;
}
