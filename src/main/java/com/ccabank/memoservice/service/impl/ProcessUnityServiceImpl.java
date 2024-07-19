package com.ccabank.memoservice.service.impl;

import com.ccabank.memoservice.constant.AppError;
import com.ccabank.memoservice.domain.AppServiceResult;
import com.ccabank.memoservice.dto.memo.ProcessUnityDto;
import com.ccabank.memoservice.entity.*;
import com.ccabank.memoservice.mappers.ProcessUnityMapper;
import com.ccabank.memoservice.repository.ProcessUnityRepository;
import com.ccabank.memoservice.service.faces.ProcessUnityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ccabank.memoservice.constant.BeanIdConstant.MEMO_SERVICE;


@Service
@Transactional
@Qualifier(MEMO_SERVICE)
public class ProcessUnityServiceImpl implements ProcessUnityService {

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    private ProcessUnityMapper processUnityMapper;

    @Autowired
    private ProcessUnityRepository processUnityRepository;





    @Override
    public AppServiceResult<ProcessUnity> create(ProcessUnityDto processUnityDto) {
        try {
            logger.info(MEMO_SERVICE + "create : methode invocation");
            ProcessUnity processUnity = processUnityMapper.toEntity(processUnityDto);
            processUnity = processUnityRepository.save(processUnity);

            return new AppServiceResult<ProcessUnity>(true, 0, "Succeed!", processUnity );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " create : Exception {}", e.getMessage());
            return new AppServiceResult<ProcessUnity>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<ProcessUnity> update(ProcessUnityDto processUnityDto) {
        try {
            logger.info(MEMO_SERVICE + "create : methode invocation");

            ProcessUnity processUnity = processUnityRepository.findById(processUnityDto.getId()).orElse(null);

            if (processUnity == null) {
                logger.warn("update : department not found -> " + processUnityDto.getId());
                return new AppServiceResult<ProcessUnity>(false, HttpStatus.NOT_FOUND.value(), "department not found!", null);
            }

            processUnity.setCode(processUnityDto.getCode());
            processUnity.setName(processUnityDto.getName());
            processUnity.setStaffList(processUnityDto.getStaffList());
            processUnity = processUnityRepository.save(processUnity);

            return new AppServiceResult<ProcessUnity>(true, 0, "Succeed!", processUnity );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " create : Exception {}", e.getMessage());
            return new AppServiceResult<ProcessUnity>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<ProcessUnityDto> getDetail(Long id) {
        try {
            logger.info(MEMO_SERVICE + "getDetail : methode invocation");

            ProcessUnity unity = processUnityRepository.getOne(id);

            ProcessUnityDto unityDto = processUnityMapper.toDto(unity);

            return new AppServiceResult<ProcessUnityDto>(true, 0, "Succeed!", unityDto );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " create : Exception {}", e.getMessage());
            return new AppServiceResult<ProcessUnityDto>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    @Override
    public AppServiceResult<List<ProcessUnityDto>> getAll() {
        try {
            logger.info(MEMO_SERVICE + "getAll : methode invocation");

            List<ProcessUnity> unities = processUnityRepository.findAll();

            List<ProcessUnityDto> unityDtos = this.getConvertedResult(unities, "getAll").getData();

            return new AppServiceResult<List<ProcessUnityDto>>(true, 0, "Succeed!", unityDtos );

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(MEMO_SERVICE + " create : Exception {}", e.getMessage());
            return new AppServiceResult<List<ProcessUnityDto>>(false, AppError.Unknown.errorCode(), e.getMessage(), null);

        }
    }

    private AppServiceResult<List<ProcessUnityDto>> getConvertedResult(List<ProcessUnity> processUnities, String functionName) {
        if (processUnities == null) {
            logger.warn(MEMO_SERVICE, functionName,
                    "Feedback not exist!, Cannot further process!");
            return new AppServiceResult<List<ProcessUnityDto>>(false, AppError.Validattion.errorCode(),
                    "Process Unity not exist!", null);
        }
        List<ProcessUnityDto> result =  new ArrayList<ProcessUnityDto>();
        if (processUnities.size() > 0) {
            for (ProcessUnity processUnity : processUnities) {
                result.add(processUnityMapper.toDto(processUnity));
            }
        }
        return new AppServiceResult<List<ProcessUnityDto>>(true, 0, "Succeed!", result);
    }



}
