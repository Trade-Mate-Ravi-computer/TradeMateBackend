package com.trademate.project.Service;

import com.trademate.project.Model.ReceivedMoneyModel;
import com.trademate.project.Repository.ReceivedMoneyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.sound.midi.Receiver;

@Service
public class RecevivedMoneyService {
    @Autowired
    private ReceivedMoneyRepository receivedMoneyRepository;


    public ResponseEntity<ReceivedMoneyModel> addMoney(ReceivedMoneyModel model){
        return new ResponseEntity<ReceivedMoneyModel>(receivedMoneyRepository.save(model), HttpStatus.CREATED);
    }
}
