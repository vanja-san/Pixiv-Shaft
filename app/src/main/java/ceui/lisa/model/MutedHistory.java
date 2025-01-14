package ceui.lisa.model;

import java.util.List;

public class MutedHistory {


    /**
     * muted_tags : [{"tag":{"name":"極上の乳"},"is_premium_slot":false}]
     * muted_users : [{"user":{"id":1236873,"name":"カオミン","account":"shibu11","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/09/09/14/08/06/14754860_a5717bca9f221b0ebabad8a2b2b34d5b_170.jpg"}},"is_premium_slot":false}]
     * muted_count : 1
     * muted_tags_count : 1
     * muted_users_count : 0
     * mute_limit_count : 1
     */

    private int muted_count;
    private int muted_tags_count;
    private int muted_users_count;
    private int mute_limit_count;
    private List<MutedTagsBean> muted_tags;
    private List<MutedUsersBean> muted_users;

    public int getMuted_count() {
        return muted_count;
    }

    public void setMuted_count(int muted_count) {
        this.muted_count = muted_count;
    }

    public int getMuted_tags_count() {
        return muted_tags_count;
    }

    public void setMuted_tags_count(int muted_tags_count) {
        this.muted_tags_count = muted_tags_count;
    }

    public int getMuted_users_count() {
        return muted_users_count;
    }

    public void setMuted_users_count(int muted_users_count) {
        this.muted_users_count = muted_users_count;
    }

    public int getMute_limit_count() {
        return mute_limit_count;
    }

    public void setMute_limit_count(int mute_limit_count) {
        this.mute_limit_count = mute_limit_count;
    }

    public List<MutedTagsBean> getMuted_tags() {
        return muted_tags;
    }

    public void setMuted_tags(List<MutedTagsBean> muted_tags) {
        this.muted_tags = muted_tags;
    }

    public List<MutedUsersBean> getMuted_users() {
        return muted_users;
    }

    public void setMuted_users(List<MutedUsersBean> muted_users) {
        this.muted_users = muted_users;
    }

    public static class MutedTagsBean {
        /**
         * tag : {"name":"極上の乳"}
         * is_premium_slot : false
         */

        private TagBean tag;
        private boolean is_premium_slot;

        public TagBean getTag() {
            return tag;
        }

        public void setTag(TagBean tag) {
            this.tag = tag;
        }

        public boolean isIs_premium_slot() {
            return is_premium_slot;
        }

        public void setIs_premium_slot(boolean is_premium_slot) {
            this.is_premium_slot = is_premium_slot;
        }

        public static class TagBean {
            /**
             * name : 極上の乳
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class MutedUsersBean {
        /**
         * user : {"id":1236873,"name":"カオミン","account":"shibu11","profile_image_urls":{"medium":"https://i.pximg.net/user-profile/img/2018/09/09/14/08/06/14754860_a5717bca9f221b0ebabad8a2b2b34d5b_170.jpg"}}
         * is_premium_slot : false
         */

        private UserBean user;
        private boolean is_premium_slot;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public boolean isIs_premium_slot() {
            return is_premium_slot;
        }

        public void setIs_premium_slot(boolean is_premium_slot) {
            this.is_premium_slot = is_premium_slot;
        }

        public static class UserBean {
            /**
             * id : 1236873
             * name : カオミン
             * account : shibu11
             * profile_image_urls : {"medium":"https://i.pximg.net/user-profile/img/2018/09/09/14/08/06/14754860_a5717bca9f221b0ebabad8a2b2b34d5b_170.jpg"}
             */

            private int id;
            private String name;
            private String account;
            private ProfileImageUrlsBean profile_image_urls;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public ProfileImageUrlsBean getProfile_image_urls() {
                return profile_image_urls;
            }

            public void setProfile_image_urls(ProfileImageUrlsBean profile_image_urls) {
                this.profile_image_urls = profile_image_urls;
            }

            public static class ProfileImageUrlsBean {
                /**
                 * medium : https://i.pximg.net/user-profile/img/2018/09/09/14/08/06/14754860_a5717bca9f221b0ebabad8a2b2b34d5b_170.jpg
                 */

                private String medium;

                public String getMedium() {
                    return medium;
                }

                public void setMedium(String medium) {
                    this.medium = medium;
                }
            }
        }
    }
}
