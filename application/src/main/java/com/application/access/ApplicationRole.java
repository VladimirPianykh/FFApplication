package com.application.access;

import com.futurefactory.User;

/**
 * Невнятное тз:
 * Создать объект для регистрации заказа от клиента на лесопродукцию.
 * Заказы на лесопродукцию должны быть доступны сотрудникам коммерческой службы,
 * сотрудникам службы производства.
 * <p>
 * А выше написано:
 * - Коммерческая служба принимает заказы от покупателей на разные виды лесопродукции.
 * - Задача службы производства – диспетчеризация и выпуск лесопродукции.
 * <p>
 * Не ясно можно ли создавать заказы продуктовой службе
 */
public enum ApplicationRole implements User.Role {
    COMMERCIAL_SERVICE(new ApplicationPermission[]{
            ApplicationPermission.CREATE_ORDER,
            ApplicationPermission.GET_ORDERS_INFO,
            ApplicationPermission.GET_PRODUCT_TYPES_INFO,
            ApplicationPermission.GET_CLIENT_INFO
    }),
    PRODUCTION_SERVICE(new ApplicationPermission[]{
            ApplicationPermission.CREATE_ORDER,
            ApplicationPermission.GET_ORDERS_INFO,
            ApplicationPermission.GET_PRODUCT_TYPES_INFO,
    }),
    TECH_SERVICE(new ApplicationPermission[]{
            ApplicationPermission.GET_PRODUCT_TYPES_INFO
    });
    // STOREKEEPER,
    // ENGINEER,
    // TESTER,
    // PRODUCTION_MANAGER,
    // PROCUREMENT_MANAGER,
    // PD_MANAGER,
    // SD_MANAGER,
    // SALES_MANAGER,

    public final ApplicationPermission[] permissions;

    ApplicationRole(ApplicationPermission[] permissions) {
        this.permissions = permissions;
    }
}
