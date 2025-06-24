package com.pragma.domain.transfer.response.movement;

import com.pragma.domain.commons.pageable.Pageable;

import java.util.List;

public record MovementResponse(
        AccountInfo accountInfo,
        List<Movement> movementsLists,
        Pageable pageable
) { }
