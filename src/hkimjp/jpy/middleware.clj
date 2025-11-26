(ns hkimjp.jpy.middleware
  (:require
   [environ.core :refer [env]]
   [hiccup2.core :as h]
   [ring.util.response :as resp]
   [taoensso.telemere :as t]))

(defn wrap-users [handler]
  (fn [{{:keys [identity]} :session :as request}]
    (t/log! :debug (str "wrap-users " identity))
    (if (some? identity)
      (do
        (t/log! :debug "found")
        (handler request))
      (do
        (t/log! :debug "not found")
        (-> (resp/redirect "/")
            (assoc :session {} :flash "need login"))))))

(defn wrap-admin [handler]
  (fn [{{:keys [identity]} :session :as request}]
    (t/log! :debug (str "wrap-admin " identity))
    (if (= (env :admin) identity)
      (handler request)
      (-> (resp/redirect "/")
          (assoc :session {} :flash "admin only")))))

(defn wrap-hx [handler]
  (fn [req]
    (-> (handler req)
        h/html
        str
        resp/response
        (resp/content-type "text/html"))))
