package com.change.gic.modules.business.process.document.listener;

import com.change.gic.modules.business.entity.DocumentType;
import com.change.gic.modules.business.repository.DocumentTypeRepository;
import com.change.gic.modules.core.entity.Document;
import com.change.gic.modules.core.repository.DocumentRepository;
import com.change.gic.modules.file.entity.File;
import com.change.gic.modules.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddDocumentCompletedListener implements ExecutionListener {

    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentRepository documentRepository;
    private final FileRepository fileRepository;

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        String documentType = (String) delegateExecution.getVariable("document_type");
        DocumentType type = documentTypeRepository.findByTag(documentType);
        String fileId = (String) delegateExecution.getVariable("document");
        File file = fileRepository.findByUrl(fileId);
        Document document = documentRepository.findByBusinessKeyAndTag(delegateExecution.getProcessBusinessKey(), documentType);
        if(document != null){
            if(!type.getMultiple()){
                documentRepository.delete(document);
            }
        }
        document = new Document();
        document.setTag(documentType);
        String firstName = (String) delegateExecution.getVariable("firstName");
        String lastName = (String) delegateExecution.getVariable("lastName");
        document.setLabel(type.getName() + " - " + firstName + " " + lastName);
        document.setBusinessKey(delegateExecution.getProcessBusinessKey());
        document.setFile(file);
        documentRepository.save(document);

    }
}
