package com.myApp.hangar.dao;

import com.myApp.hangar.model.BasicDataHangar;
import com.myApp.hangar.model.Hangar;
import com.myApp.hangar.repository.HangarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class HangarDaoImpl implements HangarDao {
	
	@Autowired
	public HangarRepository hangarRepository;
	
	public List<Hangar> getAllHangars() {
		
		List<Hangar> hangars = hangarRepository.findAll();
		if(hangars !=null)  
			return hangars;
		return null;
	}

    @Override
    public List<Hangar> findHangarsByName(String name) {
	    return hangarRepository.findByNameWithTrueState(name);
    }

    @Override
    public Boolean existHangarByName(String name) {
	   return hangarRepository.existHangarByName(name);
    }

    @Override
	public Hangar getHangar(Long id) {
		
		Hangar hangar = hangarRepository.getOne(id);
		if(hangar != null)
			return hangar;
		return null;
	}

	@Override
	public Hangar createHangar(Hangar hangar) {
		if(hangarRepository.findHangarByNameAndAddress(hangar.getName(), hangar.getAddress()) == null)
			return hangarRepository.save(hangar);
		return null;
	}

	/*@Override
	public Hangar deleteHangar(Long id) {
		Hangar com.myHangar.hangar = hangarRepository.getOne(id);
		if(com.myHangar.hangar != null)
			hangarRepository.delete(com.myHangar.hangar);
		return null;
	}*/

	@Override
	public boolean existHangar(Long id) {
		return hangarRepository.existsById(id);
	}

    @Override
    public Hangar updateHangar(Hangar hangar) {
        return hangarRepository.save(hangar);
    }

    @Override
    public List<Object> getBasicDataHangars() {
        return hangarRepository.getBasicHangars();
    }
}
