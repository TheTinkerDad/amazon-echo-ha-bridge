package com.armzilla.ha.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.armzilla.ha.persistence.entity.DeviceDescriptor;

/**
 * Created by arm on 4/13/15.
 */
public interface DeviceRepository extends CrudRepository<DeviceDescriptor, String> {
	
    Page<DeviceDescriptor> findByDeviceType(String type, Pageable request);
    
    List<DeviceDescriptor> findAll();
    
    Optional<DeviceDescriptor> findById(String id);
}
