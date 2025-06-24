package com.pragma.domain.transfer.request;

import com.pragma.domain.commons.pageable.Pageable;

public record RequestMovement(Pageable pageable, String numberAccount) {}
