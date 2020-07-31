package com.eurodyn.qlack.rulesdemo.service;

import com.eurodyn.qlack.rulesdemo.dto.WorkingSetDTO;
import com.eurodyn.qlack.rulesdemo.model.WorkingSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkingSetService extends BaseService<WorkingSetDTO, WorkingSet> {
}
