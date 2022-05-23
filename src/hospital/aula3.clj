(ns hospital.aula3
  (:use [clojure.pprint])
  (:require [hospital.model :as h.model]
            [hospital.logic :as h.logic]))

;simbolo que qq tread que acessar esse namespace pode alterar
(def nome "guilheme")

;redefinir o simbolo (refiz o binding)
(def nome 1234)

(let [nome "guilheme"]
  ;coisa 1
  ;coisa 2
  (println nome)
  ;nao estou refazendo o binding do simbolo localmente
  ;criando um novo simbolo local a este bloco
  ;SHADOWING
  (let [nome "daniela"]
    ;coisa 3
    ;coisa 4
    (println nome))
  (println nome))


(defn testa-atom []
  (let [hospital-tavares (atom { :espera hospital.model/fila-vazia})]
    (pprint hospital-tavares)
    (pprint hospital-tavares)
    (pprint (deref hospital-tavares))
    (pprint @hospital-tavares)

    ;nao eh assim que faz alteracao no conteudo dentro de um atomo
    (pprint (assoc @hospital-tavares :laboratorio1 h.model/fila-vazia))
    (pprint @hospital-tavares)

    ;essa é (uma das) maneiras de aletrar conteudo dentro de atomo
    (swap! hospital-tavares assoc :laboratorio1 h.model/fila-vazia)
    (pprint @hospital-tavares)

    (swap! hospital-tavares assoc :laboratorio2 h.model/fila-vazia)
    (pprint @hospital-tavares)

    ;update tradicional imutavel, que nao trara efeito colateral
    (pprint (update @hospital-tavares :laboratorio1 conj "111"))
    (println @hospital-tavares)

    ; indo pra swap
    (swap! hospital-tavares update :laboratorio1 conj "111")
    (pprint hospital-tavares)

    ))

;(testa-atom)

(defn chega-em-malvado! [hospital pessoa]
  (swap! hospital h.logic/chega-em-pausado-logando :laboratorio1 pessoa)
  (println "apos inserir" pessoa))



(defn simula-um-dia-em-parelelo
  []
  (let [hospital (atom (h.model/novo-hospital))]
    (.start (Thread. (fn [] (chega-em-malvado! hospital "111"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "222"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "333"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "444"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "555"))))
    (.start (Thread. (fn [] (chega-em-malvado! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))
;Forçando sitaçao de retry
(simula-um-dia-em-parelelo)


;(defn chega-sem-malvado! [hospital pessoa]
;  (swap! hospital h.logic/chega-em :espera pessoa)
;  (println "apos inserir" pessoa))
;
;(defn simula-um-dia-em-parelelo
;  []
;  (let [hospital (atom (h.model/novo-hospital))]
;    (.start (Thread. (fn [] (chega-sem-malvado! hospital "111"))))
;    (.start (Thread. (fn [] (chega-sem-malvado! hospital "222"))))
;    (.start (Thread. (fn [] (chega-sem-malvado! hospital "333"))))
;    (.start (Thread. (fn [] (chega-sem-malvado! hospital "444"))))
;    (.start (Thread. (fn [] (chega-sem-malvado! hospital "555"))))
;    (.start (Thread. (fn [] (chega-sem-malvado! hospital "666"))))
;    (.start (Thread. (fn [] (Thread/sleep 8000)
;                       (pprint hospital))))))
;;sem forçar situaçaio de retry (busy retry), pode ou nao acontecer
;(simula-um-dia-em-parelelo)


