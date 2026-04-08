(ns hkimjp.jpy.login
  (:require
   [buddy.hashers :as hashers]
   [charred.api :as charred]
   [environ.core :refer [env]]
   [org.httpkit.client :as hk-client]
   [hiccup2.core :as h]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.util.response :as resp]
   [taoensso.telemere :as t]
   [hkimjp.jpy.util :refer [user btn]]
   [hkimjp.jpy.view :refer [page]]))

(when-not (env :auth)
  (t/log! :error "AUTH not defined")
  (System/exit 1))

(defn login
  [request]
  (page
   [:div.mx-4
    [:div.font-bold.p-2 "LOGIN" (when (env :develop) " (DEVELOP)")]
    (when-let [flash (:flash request)]
      [:div {:class "text-red-500"} flash])
    [:div.p-1
     [:form {:method "post"}
      (h/raw (anti-forgery-field))
      [:input.border-1.px-1.rounded
       {:name "login" :placeholder "account" :autocomplete "username"}]
      [:span.mx-1 ""]
      [:input.border-1.px-1.rounded
       {:name "password" :type "password" :placeholder "password" :autocomplete "current-password"}]
      [:button {:class btn} "LOGIN"]]]
    [:br]]))

(comment
  (-> (hk-client/get (str (env :auth) "tue3"))
      deref
      :body)

  :rcf)

(defn login!
  [{{:keys [login password]} :params}]
  (t/log! {:level :debug :id "login!" :msg (str login " " password)})
  (try
    (let [pw (-> (hk-client/get (str (env :auth) login))
                 deref
                 :body
                 (charred/read-json :key-fn keyword)
                 :password)]
      (when (hashers/check password pw)
        (t/log! :info (str "login success: " login)))
      (-> (resp/redirect "/workspace")
          (assoc-in [:session :identity] login)))
    (catch Exception e
      (t/log! :info (str "login failed: " login))
      (-> (resp/redirect "/")
          (assoc :session {} :flash "login failed")))))

(defn logout! [request]
  (t/log! {:level :info
           :id "logout!"
           :msg (user request)})
  (-> (resp/redirect "/")
      (assoc :session {})))
