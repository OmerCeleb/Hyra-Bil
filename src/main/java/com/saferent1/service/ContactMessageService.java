package com.saferent1.service;

import com.saferent1.domain.ContactMessage;
import com.saferent1.repository.ContactMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageService(ContactMessageRepository contactMessageRepository) {
        this.contactMessageRepository = contactMessageRepository;
    }


    public void saveMessage(ContactMessage contactMessage) {
        contactMessageRepository.save(contactMessage);
    }


    public List<ContactMessage> getAll() {
        return contactMessageRepository.findAll();
    }

    public Page<ContactMessage> getAll(Pageable pageable){
        return contactMessageRepository.findAll(pageable);
    }

}
