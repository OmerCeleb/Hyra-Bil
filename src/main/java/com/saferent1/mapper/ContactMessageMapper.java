package com.saferent1.mapper;

import com.saferent1.domain.ContactMessage;
import com.saferent1.dto.ContactMessageDTO;
import com.saferent1.dto.request.ContactMessageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


//För att använda mapstruct beroendet är det nödvändigt att skapa ett interface.
//det här biblioteket gör omvandlingarna själv efter att ha lagt till @Mapper kommentaren
@Mapper(componentModel = "spring") // Jag kan injicera och använda vilken klass som helst.
public interface ContactMessageMapper {

    //!!! ContactMessage ---> ContactMessageDTO
    ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage);

    //!!! ContactMessageRequest --> ContactMessage
    @Mapping(target = "id", ignore = true)
    //ContactMessageRequest de id olmamasi icin mapleme yapilmamasini belirtiyoruz.
    ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);

    //!!! List<ContactMessage> --> List<ContactMessageDTO>
    List<ContactMessageDTO> map(List<ContactMessage> contactMessageList); //getAllContactMessage()

}
