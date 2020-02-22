package com.example.mycloudmusic.domain;

/**
 * 资源
 * 将资源放到单独的对象中
 * 好处是后面还可扩展更多的字段
 * 例如：资源类型；资源大小；资源备注
 */
public class Resource {
    /**
     * 相对地址
     */
    private String uri;

    public Resource() {
    }

    public Resource(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resource{");
        sb.append("uri='").append(uri).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
