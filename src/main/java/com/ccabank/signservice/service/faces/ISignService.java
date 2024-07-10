package com.ccabank.signservice.service.faces;

import com.ccabank.signservice.domain.AppServiceResult;
import com.ccabank.signservice.dto.sign.SignatureRequestDto;

public interface ISignService {

    AppServiceResult<SignatureRequestDto> sign(SignatureRequestDto dto);
}
