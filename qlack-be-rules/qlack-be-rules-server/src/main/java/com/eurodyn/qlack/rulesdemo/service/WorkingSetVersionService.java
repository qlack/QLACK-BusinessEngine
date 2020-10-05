package com.eurodyn.qlack.rulesdemo.service;

import com.eurodyn.qlack.rulesdemo.dto.WorkingSetVersionDTO;
import com.eurodyn.qlack.rulesdemo.model.WorkingSetVersion;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkingSetVersionService extends BaseService<WorkingSetVersionDTO, WorkingSetVersion> {
}
