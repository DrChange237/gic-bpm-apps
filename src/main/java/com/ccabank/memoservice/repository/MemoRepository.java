package com.ccabank.memoservice.repository;

import com.ccabank.memoservice.entity.File;
import com.ccabank.memoservice.entity.documenttype.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long>  {
}
