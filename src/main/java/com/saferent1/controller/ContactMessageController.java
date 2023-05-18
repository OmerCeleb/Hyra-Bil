package com.saferent1.controller;

import com.saferent1.domain.ContactMessage;
import com.saferent1.dto.ContactMessageDTO;
import com.saferent1.dto.request.ContactMessageRequest;
import com.saferent1.dto.response.SfResponse;
import com.saferent1.mapper.ContactMessageMapper;
import com.saferent1.service.ContactMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//***Vi gjorde DTO  POJO-konverteringar för meddelandeslutpunkter endast den här gången på kontrollskiktet.
// Det bästa med det här jobbet är att göra dessa omvandlingar i servicepackage.

@RestController // -- > RestfullApi
@RequestMapping("/kontaktmeddelande")
public class ContactMessageController {

    // Skäl till att välja Field Injection;
    //1)Säkerhet(Normalt sett borde jag inte kunna komma åt ett privat fält, men @Autowired öppnar dörrarna som skapar säkerhetslösenordet för oss som ReflectAPI på baksidan)
    //2) Eftersom field är privat kan det inte ses från andra klasser att injektionen görs över denna datatyp.

    //@Autowired ->  Kommenterade för att undvika fältinjektion
    private final ContactMessageService contactMessageService;

    //@Autowired -->Det finns inget behov av detta. Spring framework är så smart att det kan göra fältet final och const. om det skapas
    // säger att detta är Constructor Injection och vill inte ha @Autowired från oss

    private final ContactMessageMapper contactMessageMapper;


    public ContactMessageController(ContactMessageService contactMessageService, ContactMessageMapper contactMessageMapper) {
        this.contactMessageService = contactMessageService;
        this.contactMessageMapper = contactMessageMapper;
    }

    //!!! Create ContactMessage
    @PostMapping("/visitors")
    public ResponseEntity<SfResponse> createMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {
        // Jag kommer att använda mapStruct-strukturen för att konvertera DTO:n jag fick till POJO
        ContactMessage contactMessage =
                contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);

        contactMessageService.saveMessage(contactMessage);
        //Vi har nytt SfResponse här. Men det var inte bra att uppdatera på våren. Varför gjorde vi det.
        // För jag kommer att använda det här objektet här en gång.
        SfResponse response = new SfResponse("ContactMessage succesfully created", true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }

    //*********************************************************************************************************
    //!!! getAllContactMessages
    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllContactMessage() {
        List<ContactMessage> contactMessageList = contactMessageService.getAll();
        //mapStruct (Pojos --> DTOs)
        List<ContactMessageDTO> contactMessageDTOList = contactMessageMapper.map(contactMessageList);

        return ResponseEntity.ok(contactMessageDTOList);
        // return new ResponseEntity<>(contactMessageDTOList,HttpStatus.ok);
    }

    //*********************************************************************************************************
    //!!! Pageable ( Server Side Paging - Client Side Paging )
    @GetMapping("/pages")
    public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessageWithPage(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sort") String prop,
            @RequestParam(value = "direction",
                    required = false,
                    defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<ContactMessage> contactMessagePage = contactMessageService.getAll(pageable);
        Page<ContactMessageDTO> contactMessageDTOPage = getPageDto(contactMessagePage);
        return ResponseEntity.ok(contactMessageDTOPage);

    }

    //*********************************************************************************************************
    //!!! låt oss få det specifikt ContactMessage med en PathVariable
    @GetMapping("/{id}")
    public ResponseEntity<ContactMessageDTO> getMessageWithPath(@PathVariable("id") Long id) {
        ContactMessage contactMessage = contactMessageService.getContactMessage(id);
        ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);
        return ResponseEntity.ok(contactMessageDTO);
    }


    //!!! låt oss få det specifikt ContactMessage med en RequestParam
    @GetMapping("/request")
    public ResponseEntity<ContactMessageDTO> getMessageWithRequestParam(
            @RequestParam("id") Long id) {
        ContactMessage contactMessage = contactMessageService.getContactMessage(id);
        ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);
        return ResponseEntity.ok(contactMessageDTO);
    }


    //!!getPageDTO
    private Page<ContactMessageDTO> getPageDto(Page<ContactMessage> contactMessagePage) {
        return contactMessagePage.map( //
                contactMessageMapper::contactMessageToDTO);
    }


}