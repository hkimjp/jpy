(ns hkimjp.jpy.login
  (:require
   [buddy.hashers :as hashers]
   [charred.api :as charred]
   [environ.core :refer [env]]
   [hiccup2.core :as h]
   [org.httpkit.client :as http]
   [ring.util.anti-forgery :refer [anti-forgery-field]]
   [ring.util.response :as resp]
   [taoensso.telemere :as tel]
   [hkimjp.jpy.view :refer [page]]))

(when-not (env :auth)
  (tel/log! :error "AUTH not defined")
  (System/exit 1))

(def btn "mx-1 px-1 text-white bg-sky-500 hover:bg-sky-700 active:bg-red-500 rounded")
(def input-box "px-1 border-1 rounded")
(def section "text-2xl font-bold p-2")
(def subsection "text-xl font-bold")
(def warn "text-red-500")

(defn login [req]
  (page
   [:div.m-4
    [:div {:class section} "LOGIN"]
    [:div {:class warn} (:flash req)]
    [:form {:method :post}
     (h/raw (anti-forgery-field))
     [:input {:name "url" :type "hidden" :value "/workspace"}]
     [:input {:class        input-box
              :name         "user"
              :placeholder  "account"
              :autocomplete "username"}]
     [:input {:class        input-box
              :name         "password"
              :type         "password"
              :placeholder  "password"
              :autocomplete "current-password"}]
     [:button {:class btn} "LOGIN"]]]))

; (defn login [req]
;   (page
;    [:div.content
;     [:div "LOGIN"]
;     [:div (:flash req)]
;     [:form {:method :post}
;      (h/raw (anti-forgery-field))
;      [:input {:name "url" :type "hidden" :value "/hello"}]
;      [:label "name"] [:input {:name "user"}]
;      [:label "password"] [:input {:name "password" :type "password"}]
;      [:br]
;      [:button.button "submit"]]]))

(defn fetch [user]
  (let [auth (str (or (System/getenv "AUTH") "http://l22/api/user/") user)]
    (try
      (-> (http/get auth)
          deref
          :body
          (charred/read-json :key-fn keyword)
          :password)
      (catch Exception _ nil))))

(defn login! [{{:keys [user password url]} :params :as req}]
  (tel/log! {:level :info :id "login!"
             :data (dissoc (:params req) :__anti-forgery-token)})
  (tel/log! :debug (str "fetch user" (fetch user)))
  (if (hashers/check password (fetch user))
    (do
      (tel/log! :info (str "login success: " user))
      (-> (resp/redirect url)
          (assoc-in [:session :identity] user)))
    (do
      (tel/log! :warn (str "login failed: " user))
      (-> (resp/redirect "/")
          (assoc :session {} :flash "login failed")))))

(defn logout! [_request]
  (tel/log! {:level :info :id "logout!"})
  (-> (resp/redirect "/")
      (assoc :session {} :flash "please login")))
