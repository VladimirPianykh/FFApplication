package com.application.access;

import com.futurefactory.User;

public enum ApplicationPermission implements User.Permission {
    CREATE_ORDER,
    GET_ORDERS_INFO,
    GET_PRODUCT_TYPES_INFO,
    GET_CLIENT_INFO
}