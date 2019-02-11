package com.armzilla.ha.rest.devicemanagement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.armzilla.ha.dao.DeviceDescriptor;
import com.armzilla.ha.dao.DeviceRepository;
import com.armzilla.ha.rest.devicemanagmeent.api.Device;

/**
 * Created by arm on 4/13/15.
 * 
 * TODO: Sort out what's with Device and DeviceDescriptor - returning an entity from the DB layer is lazy...
 * TODO: Turn this whole thing into handling devices with MQTT topics and not HTTP URLS!
 */
@Controller
@RequestMapping("/api/devices")
public class DeviceManagerController {

    private static final String APPLICATION_JSON = "application/json";

	private static final Set<String> supportedVerbs = new HashSet<>(Arrays.asList("get", "put", "post"));

    @Autowired
    private DeviceRepository deviceRepository;

    @PostMapping(produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    public ResponseEntity<DeviceDescriptor> createDevice(@RequestBody Device device) {
    	
    	//TODO: add more validation like content type
        if(device.getContentBody() != null ) {
            if (device.getContentType() == null || device.getHttpVerb() == null || !supportedVerbs.contains(device.getHttpVerb().toLowerCase())) {
                return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
            }
        } 
        
        DeviceDescriptor deviceEntry = new DeviceDescriptor();
        deviceEntry.setId(UUID.randomUUID().toString());
        deviceEntry.setName(device.getName());
        deviceEntry.setDeviceType(device.getDeviceType());
        deviceEntry.setOnUrl(device.getOnUrl());
        deviceEntry.setOffUrl(device.getOffUrl());
        deviceEntry.setContentType(device.getContentType());
        deviceEntry.setContentBody(device.getContentBody());
        deviceEntry.setHttpVerb(device.getHttpVerb());

        deviceRepository.save(deviceEntry);

        return new ResponseEntity<>(deviceEntry, null, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{lightId}", produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    public ResponseEntity<DeviceDescriptor> updateDevice(@PathVariable("lightId") String id, @RequestBody Device device) {
        
    	Optional<DeviceDescriptor> deviceEntry = deviceRepository.findById(id);
        if(!deviceEntry.isPresent()){
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }

        DeviceDescriptor desc = deviceEntry.get();
        desc.setName(device.getName());
        desc.setDeviceType(device.getDeviceType());
        desc.setOnUrl(device.getOnUrl());
        desc.setOffUrl(device.getOffUrl());

        deviceRepository.save(desc);

        return new ResponseEntity<>(desc, null, HttpStatus.OK);
    }

    @GetMapping(produces = APPLICATION_JSON)
    public ResponseEntity<List<DeviceDescriptor>> findAllDevices() {
    	
        List<DeviceDescriptor> deviceList = deviceRepository.findAll();
        List<DeviceDescriptor> plainList = new LinkedList<>(deviceList);
        return new ResponseEntity<>(plainList, null, HttpStatus.OK);
    }

    @GetMapping(value = "/{lightId}", produces = APPLICATION_JSON)
    public ResponseEntity<DeviceDescriptor> findByDevicId(@PathVariable("lightId") String id){
        
    	Optional<DeviceDescriptor> descriptor = deviceRepository.findById(id);
        if(!descriptor.isPresent()){
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(descriptor.get(), null, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{lightId}", produces = APPLICATION_JSON)
    public ResponseEntity<String> deleteDeviceById(@PathVariable("lightId") String id){
        
    	Optional<DeviceDescriptor> deleted = deviceRepository.findById(id);
        if(!deleted.isPresent()){
            return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
        }
        deviceRepository.delete(deleted.get());
        return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }
}
