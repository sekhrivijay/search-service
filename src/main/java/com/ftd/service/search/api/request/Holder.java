package com.ftd.service.search.api.request;

import java.io.Serializable;

import com.ftd.service.search.api.response.Redirect;

public class Holder implements Serializable {
    private Redirect redirect;
    private boolean cacheable = true;
    private boolean isBoost;
    private boolean isRedirect;
    private boolean isFacet;

    public Redirect getRedirect() {
        return redirect;
    }

    public void setRedirect(Redirect redirect) {
        this.redirect = redirect;
    }

    public Boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(Boolean cacheable) {
        this.cacheable = cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public boolean isBoost() {
        return isBoost;
    }

    public void setBoost(boolean boost) {
        isBoost = boost;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public void setRedirect(boolean redirect) {
        isRedirect = redirect;
    }

    public boolean isFacet() {
        return isFacet;
    }

    public void setFacet(boolean facet) {
        isFacet = facet;
    }

    @Override
    public String toString() {
        return "Holder{" +
                "redirect=" + redirect +
                ", cacheable=" + cacheable +
                ", isBoost=" + isBoost +
                ", isRedirect=" + isRedirect +
                ", isFacet=" + isFacet +
                '}';
    }


    public static final class HolderBuilder {
        private Redirect redirect;
        private boolean cacheable = true;
        private boolean isBoost;
        private boolean isRedirect;
        private boolean isFacet;

        private HolderBuilder() {
        }

        public static HolderBuilder aHolder() {
            return new HolderBuilder();
        }

        public HolderBuilder withRedirect(Redirect redirect) {
            this.redirect = redirect;
            return this;
        }

        public HolderBuilder withCacheable(boolean cacheable) {
            this.cacheable = cacheable;
            return this;
        }

        public HolderBuilder withIsBoost(boolean isBoost) {
            this.isBoost = isBoost;
            return this;
        }

        public HolderBuilder withIsRedirect(boolean isRedirect) {
            this.isRedirect = isRedirect;
            return this;
        }

        public HolderBuilder withIsFacet(boolean isFacet) {
            this.isFacet = isFacet;
            return this;
        }

        public Holder build() {
            Holder holder = new Holder();
            holder.setRedirect(redirect);
            holder.setCacheable(cacheable);
            holder.isRedirect = this.isRedirect;
            holder.isFacet = this.isFacet;
            holder.isBoost = this.isBoost;
            return holder;
        }
    }
}
