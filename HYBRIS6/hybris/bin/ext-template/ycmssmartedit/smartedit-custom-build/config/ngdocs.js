/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
module.exports = function() {

    return {
        targets: ['ycmssmartedit'],
        config: function(data, conf) {
            return {
                options: {
                    dest: 'jsTarget/docs',
                    title: "ycmssmartedit API",
                    startPage: '/#/ycmssmartedit',
                },
                ycmssmartedit: {
                    api: true,
                    src: ['web/features/common/**/*.js', 'web/features/ycmssmartedit/**/*.js', 'web/features/common/**/*.ts', 'web/features/ycmssmartedit/**/*.ts'],
                    title: 'ycmssmartedit'
                }
            };
        }
    };
};