package com.eurodyn.qlack.rulesdemo.service;

import com.eurodyn.qlack.rulesdemo.dto.RuleDTO;
import com.eurodyn.qlack.rulesdemo.model.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RuleService extends BaseService<RuleDTO, Rule> {
}
