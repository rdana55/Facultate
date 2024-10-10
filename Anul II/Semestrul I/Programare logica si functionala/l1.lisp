;Problema 7

;a

;liniara (l: list)

(defun liniara(l)
	(cond
        	((null l) t)
        	((listp (car l)) nil)
        	(t (liniara (cdr l)))
    	)
)


;b

;apare(l: list, elem: element)
;l: lista in care cautam
;elem: elementul cautat

(defun apare(l elem)
	(cond
        	((null l) nil)
        	((equal (car l) elem) t)
        	((listp (car l)) (OR (apare (car l) elem) (apare (cdr l) elem)))
        	(t (apare (cdr l) elem))
    	)
)

;inlocuire(l:list, e1_v: element, e1_n: element)
;l: lista initiala
;e1_v: elementul cautat 
;e1_n: elementul cu care se inlocuieste

(defun inlocuire(l el_v el_n)
  	(cond
    	((null l) nil)
    	((equal (car l) el_v) (cons el_n (cdr l)))
    	((and (listp (car l))) (cons (inlocuire (car l) el_v el_n) (inlocuire(cdr l) el_v el_n)))
    	(t (cons (car l) (inlocuire (cdr l) el_v el_n)))
  	)
)


;c

;inverseaza_total(l: list)

(defun inverseaza_total(l)
  	(cond
    	((null l) nil)
    	((listp (car l)) (append (inverseaza_total(cdr l)) (inverseaza_total(car l))))
    	(T (append (inverseaza_total(cdr l)) (list(car l))))
  	)
)

;mainC(l: list)

(defun mainC(l)
    	(cond
        	((null l) nil)
        	((listp (car l)) (append (list (car (inverseaza_total (car l)))) (mainC (cdr l)) ))
        	(t (append (list (car l)) (mainC (cdr l))))
    	)
)


;d

;interclasare(l: list, p: list)

(defun interclasare(l p)
	(cond
		((null p) l)
		((and (null l) (not (null p))) p)
		((<(car l)(car p))(cons(car l)(interclasare(cdr l) p)))
		((>(car l)(car p))(cons(car p)(interclasare l (cdr p))))
		(t(cons(car l)(interclasare(cdr l)(cdr p))))
	)
)


;elimina_dubluri(l: list)

(defun elimina_dubluri(l)
    	(cond
        	((null l) nil)
        	((null (cdr l)) (list (car l)))
        	((equal (car l) (car (cdr l))) (elimina_dubluri (cdr l)))
        	(t (append (list (car l)) (elimina_dubluri (cdr l))))
    	)
)

;mainD(l1: list, l2:list)

(defun mainD(l1 l2)
    	(elimina_dubluri (interclasare l1 l2))
)