package com.eurodyn.qlack.rulesdemo.service;

import com.eurodyn.qlack.rulesdemo.dto.WorkingSetVersionDTO;
import com.eurodyn.qlack.rulesdemo.model.QWorkingSetVersion;
import com.eurodyn.qlack.rulesdemo.model.WorkingSetVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkingSetVersionService extends BaseService<WorkingSetVersionDTO, WorkingSetVersion> {
}
