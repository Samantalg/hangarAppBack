package com.myApp.hangar.service;

import com.myApp.hangar.model.BasicDataHangar;
import com.myApp.hangar.model.Hangar;

import java.util.List;

public interface HangarService {
	
	List<Hangar> getAllHangars();
	
	Hangar getHangar(long id);
	
	Hangar createHangar(Hangar hangar);
	
	//Hangar deleteHangar(long id);

	boolean hangarExist(Hangar hangar);

	boolean hangarExistById(long id);

	Hangar modifyHangar(Hangar update);

    Hangar updateState(Long id);

	List<Object> getColumnIdAndName();
	
}
