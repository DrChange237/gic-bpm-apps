package com.ccabank.signservice.service.impl;


import com.ccabank.signservice.constant.AppError;
import com.ccabank.signservice.domain.AppServiceResult;
import com.ccabank.signservice.dto.sign.FileDto;
import com.ccabank.signservice.dto.sign.SignDocumentDto;
import com.ccabank.signservice.dto.sign.SignatureRequestDto;
import com.ccabank.signservice.entity.Document;
import com.ccabank.signservice.entity.SignatureRequest;
import com.ccabank.signservice.entity.SignatureRequestStatus;
import com.ccabank.signservice.openfeign.FileRestClient;
import com.ccabank.signservice.repository.DocumentRepository;
import com.ccabank.signservice.repository.SignatureRequestRepository;
import com.ccabank.signservice.service.faces.ISignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.ccabank.signservice.constant.BeanIdConstant.SIGN_SERVICE;

@Service
@Transactional
@Qualifier(SIGN_SERVICE)
public class SignService implements ISignService {

    private static final Logger logger = LoggerFactory.getLogger(SignService.class);

    @Autowired
    private SignatureRequestRepository signatureRequestRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private FileRestClient fileRestClient;





    @Override
    public AppServiceResult<SignatureRequestDto> sign(SignatureRequestDto dto) {
        try {
            logger.info(SIGN_SERVICE + "sign : methode invocation");

            SignatureRequest signatureRequest = new SignatureRequest();
            signatureRequest.setCreatedAt(LocalDateTime.now());
            signatureRequest.setSignatureRequestStatus(SignatureRequestStatus.PENDING);

            signatureRequest = this.signatureRequestRepository.save(signatureRequest);

            FileDto file = new FileDto();


            for(SignDocumentDto documentDto :  dto.getDocuments() ){
                  file = this.signDocument(documentDto);
                  Document document = new Document();
                  document.setFileIdSigned(file.getId());
                  document.setFilePathSigned(file.getUrl());

            }

            return new AppServiceResult<SignatureRequestDto>(true, 0, "Succeed!", dto );
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(SIGN_SERVICE + " addFeedback : Exception {}", e.getMessage());
            return new AppServiceResult<SignatureRequestDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    public FileDto signDocument(SignDocumentDto documentDto){

        //Recupération du fichier depuis file-service
        FileDto file = this.fileRestClient.getFileById(documentDto.getFileId());

        // Signer le document avec la signature des différents Users


        // Enregistrer le document signé

        FileDto fileSigned = this.fileRestClient.uploadFileToFolder("sign-service", "", null );

        return fileSigned;

    }


    private AppServiceResult<SignatureRequestDto> getConvertedResult(SignatureRequest request, String functionName) {
        if (request == null) {
            logger.warn(SIGN_SERVICE, functionName,
                    "Feedback not exist!, Cannot further process!");
            return new AppServiceResult<SignatureRequestDto>(false, AppError.Validattion.errorCode(),
                    "Feedback not exist!", null);
        }
        SignatureRequestDto result =  new SignatureRequestDto();

        return new AppServiceResult<SignatureRequestDto>(true, 0, "Succeed!", result);
    }

}
