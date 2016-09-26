package com.merle.scheme;

public enum CommScheme {

    USER {
        @Override
        public String getName() {
            return "user";
        }
    }, PAGE_SIZE {
        @Override
        public String getName() {
            return "pageSize";
        }
    }, PAGE_INDEX {
        @Override
        public String getName() {
            return "pageIndex";
        }
    }, START {
        @Override
        public String getName() {
            return "start";
        }
    }, END {
        @Override
        public String getName() {
            return "end";
        }
    }, LIMIT {
        @Override
        public String getName() {
            return "limit";
        }
    }, VERSION {
        @Override
        public String getName() {
            return "version";
        }
    }, SUCCESS {
        @Override
        public String getName() {
            return "success";
        }
    }, FAIL {
        @Override
        public String getName() {
            return "fail";
        }
    };

    public abstract String getName();
}
