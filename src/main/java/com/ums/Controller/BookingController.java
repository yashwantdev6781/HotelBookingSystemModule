package com.ums.Controller;

import com.ums.Repository.BookingRepository;
import com.ums.Repository.PropertyRepository;
import com.ums.Service.BucketService;
import com.ums.Service.PDFService;
import com.ums.Service.TwilioSmsService;
import com.ums.entity.AppUser;
import com.ums.entity.Booking;
import com.ums.entity.Property;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    private BucketService bucketService;

    private PDFService pdfService;
    private BookingRepository bookingRepository;

    private TwilioSmsService twilioSmsService;
    private PropertyRepository propertyRepository;

    public BookingController(BucketService bucketService, PDFService pdfService, BookingRepository bookingRepository, TwilioSmsService twilioSmsService, PropertyRepository propertyRepository) {
        this.bucketService = bucketService;
        this.pdfService = pdfService;
        this.bookingRepository = bookingRepository;
        this.twilioSmsService = twilioSmsService;
        this.propertyRepository = propertyRepository;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<Booking> createBooking(
    @RequestParam long propertyId,
    @RequestBody Booking booking,
    @AuthenticationPrincipal AppUser appUser
){
        Property property = propertyRepository.findById(propertyId).get();
        int nightlyPrice = property.getNightlyPrice();
        int totalPrice = nightlyPrice* booking.getTotalNights();
        // double tax = totalPrice*(18/100);
        booking.setTotalPrice(totalPrice);
        booking.setProperty(property);
        booking.setAppUser(appUser);

        Booking savedBooking = bookingRepository.save(booking);
        String filePath = pdfService.generateBookingDetailsPdf(savedBooking);

        try {

            MultipartFile fileMultiPart = BookingController.convert(filePath);
            String fileUploadedUrl =bucketService.uploadFile(fileMultiPart, "travelocity6781");
            System.out.println(fileUploadedUrl);
            sendMessage(fileUploadedUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(savedBooking, HttpStatus.CREATED);

    }




    public void sendMessage(String url) {
        twilioSmsService.sendSms("+917999101998", "Your booking has been confirmed. click here:"+url);

    }
    public static MultipartFile convert (String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        Resource resource = new ByteArrayResource(fileContent);
        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return fileContent.length == 0;
            }

            @Override
            public long getSize() {
                return fileContent.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return fileContent;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.write(dest.toPath(), fileContent);
            }

        };

        return multipartFile;
    }
}




