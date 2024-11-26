package com.application.access;

import com.futurefactory.User;

public enum ApplicationPermission implements User.Permission{
    CREATE_ORDER,
    CREATE_PRODUCTTYPE,
    CREATE_CUSTOMER,
    READ_ORDER,
    READ_PRODUCTTYPE,
    READ_CUSTOMER,
    READ_WORKSHOP,
    CREATE_WORKSHOP
}