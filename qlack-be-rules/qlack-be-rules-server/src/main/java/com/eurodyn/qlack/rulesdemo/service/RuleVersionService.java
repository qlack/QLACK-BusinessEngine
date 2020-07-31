package com.eurodyn.qlack.rulesdemo.service;

import com.eurodyn.qlack.rulesdemo.dto.RuleVersionDTO;
import com.eurodyn.qlack.rulesdemo.model.QRuleVersion;
import com.eurodyn.qlack.rulesdemo.model.RuleVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RuleVersionService extends BaseService<RuleVersionDTO, RuleVersion> {

    public List<RuleVersionDTO> findByRule(String ruleId) {
        List<RuleVersionDTO> ruleVersionDTOList = new ArrayList<>();
        repository.findAll(QRuleVersion.ruleVersion.rule.id.eq(ruleId))
                .forEach(ruleVersion -> ruleVersionDTOList.add(mapper.map(ruleVersion)));

        return ruleVersionDTOList;
    }
}
