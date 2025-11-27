(ns hkimjp.jpy.help
  (:require [hkimjp.jpy.view :refer [page]]))

(defn help [_request]
  (page
   [:div.m-4
    [:div.text-2xl.font-medium "授業中 Python"]
    [:p "workspace に表示する問題を自分のVScode/Python で解き、"
     "回答を VScode からコピペでこちらに。"]
    [:div.font-bold "workspace"]
    [:div.m-4
     [:p "回答をアップロードする。過去回答のリストが見える。"
      "回答しなかった回答？残ってるわけねーでしょ。"]]
    [:div.font-bold "scoreboard"]
    [:div.m-4
     [:p "自分の jpy 成績が見える。"]]
    [:div.font-bold "logout"]
    [:div.m-4
     [:p "新しくログインする。"]]
    [:div.font-bold "admin"]
    [:div.m-4
     [:p "管理者専用ページ。問題を投入する。"]]]))
