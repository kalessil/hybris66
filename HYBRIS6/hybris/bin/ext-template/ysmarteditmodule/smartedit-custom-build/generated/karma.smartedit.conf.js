module.exports = function(config) {
    config.set({
        "singleRun": true,
        "coverageReporter": {
            "dir": "jsTests\u002Fcoverage\u002F",
            "reporters": [{
                    "type": "html",
                    "subdir": "report-html"
                },
                {
                    "type": "cobertura",
                    "subdir": ".",
                    "file": "cobertura.xml"
                }
            ]
        },
        "junitReporter": {
            "outputDir": "jsTarget\u002Ftest\u002Fysmarteditmodule\u002Fjunit\u002F",
            "outputFile": "testReport.xml"
        },
        "files": [
            "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule\u002Fsmartedit-build\u002Fwebroot\u002Fstatic-resources\u002Fdist\u002Fsmartedit\u002Fjs\u002Fprelibraries.js",
            "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule\u002Fsmartedit-build\u002Ftest\u002Funit\u002F**\u002F*.+(js|ts)",
            "web\u002Ffeatures\u002Fysmarteditmodulecommons\u002F**\u002F*.+(js|ts)",
            "web\u002Ffeatures\u002Fysmarteditmodule\u002F**\u002F*.+(js|ts)",
            "jsTarget\u002Fweb\u002Ffeatures\u002Fysmarteditmodule\u002Ftemplates.js",
            "jsTests\u002Fysmarteditmodule\u002Funit\u002Ffeatures\u002F**\u002F*.+(js|ts)"
        ],
        "exclude": [
            "web\u002Ffeatures\u002Fysmarteditmodule\u002Fysmarteditmodule.ts",
            "**\u002F*.d.ts",
            "*.d.ts"
        ],
        "webpack": {
            "devtool": "source-map",
            "externals": {
                "jasmine": "jasmine",
                "testutils": "testutils",
                "angular-mocks": "angular-mocks",
                "angular": "angular",
                "angular-route": "angular-route",
                "angular-translate": "angular-translate"
            },
            "output": {
                "path": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule\u002FjsTarget",
                "filename": "[name].js",
                "sourceMapFilename": "[file].map"
            },
            "resolve": {
                "modules": [
                    "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule\u002FjsTarget\u002Fweb\u002Fapp",
                    "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule\u002FjsTarget\u002Fweb\u002Ffeatures"
                ],
                "extensions": [
                    ".ts",
                    ".js"
                ],
                "alias": {
                    "ysmarteditmodulecommons": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule\u002FjsTarget\u002Fweb\u002Ffeatures\u002Fysmarteditmodulecommons",
                    "ysmarteditmodule": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule\u002FjsTarget\u002Fweb\u002Ffeatures\u002Fysmarteditmodule"
                }
            },
            "module": {
                "rules": [{
                    "test": /\.ts$/,
                    "loader": "awesome-typescript-loader",
                    "options": {
                        "configFileName": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule\u002Fsmartedit-custom-build\u002Fgenerated\u002Ftsconfig.karma.smartedit.json"
                    }
                }]
            },
            "stats": {
                "colors": true,
                "modules": true,
                "reasons": true,
                "errorDetails": true
            },
            "bail": false
        },
        "basePath": "\u002Fsrv\u002Fjenkins\u002Fworkspace\u002Fcommerce-suite-unpacked\u002Fbuild\u002Fsource\u002Fysmarteditmodule",
        "frameworks": [
            "jasmine"
        ],
        "decorators": [
            "karma-phantomjs-launcher",
            "karma-jasmine"
        ],
        "preprocessors": {
            "**\u002F*.ts": [
                "webpack"
            ]
        },
        "reporters": [
            "spec",
            "junit"
        ],
        "specReporter": {
            "suppressPassed": true
        },
        "port": 9876,
        "colors": true,
        "autoWatch": false,
        "autoWatchBatchDelay": 1000,
        "browsers": [
            "PhantomJS"
        ],
        "proxies": {
            "\u002Fstatic-resources\u002Fimages\u002F": "\u002Fbase\u002Fstatic-resources\u002Fimages\u002F"
        },
        "plugins": [
            "karma-webpack",
            "karma-jasmine",
            "karma-phantomjs-launcher",
            "karma-junit-reporter",
            "karma-spec-reporter"
        ]
    });
};
