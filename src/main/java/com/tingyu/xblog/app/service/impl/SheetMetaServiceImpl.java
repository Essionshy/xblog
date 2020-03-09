package com.tingyu.xblog.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.tingyu.xblog.app.exception.NotFoundException;
import com.tingyu.xblog.app.model.entity.SheetMeta;
import com.tingyu.xblog.app.repository.SheetMetaRepository;
import com.tingyu.xblog.app.repository.SheetRepository;
import com.tingyu.xblog.app.service.SheetMetaService;

/**
 * Sheet meta service implementation class.
 *
 * @author ryanwang
 * @author ikaisec
 * @date 2019-08-04
 */
@Slf4j
@Service
public class SheetMetaServiceImpl extends BaseMetaServiceImpl<SheetMeta> implements SheetMetaService {

    private final SheetMetaRepository sheetMetaRepository;

    private final SheetRepository sheetRepository;

    public SheetMetaServiceImpl(SheetMetaRepository sheetMetaRepository,
                                SheetRepository sheetRepository) {
        super(sheetMetaRepository);
        this.sheetMetaRepository = sheetMetaRepository;
        this.sheetRepository = sheetRepository;
    }

    @Override
    public void validateTarget(Integer sheetId) {
        sheetRepository.findById(sheetId)
            .orElseThrow(() -> new NotFoundException("查询不到该页面的信息").setErrorData(sheetId));
    }
}
