package com.springboot.asm.fpoly_asm_springboot.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageUtil {
    private static int DEFAULT_PAGE_SIZE = 4;

    public Pageable createPageable(int pageNum) {
        return PageRequest.of(getPageNum(pageNum), DEFAULT_PAGE_SIZE);
    }

    public Pageable createPageable(int pageNum, int pageSize) {
        return PageRequest.of(getPageNum(pageNum), pageSize);
    }

    public int getPageNum(int pageNum) {
        return Math.max(pageNum - 1, 0);
    }
}
