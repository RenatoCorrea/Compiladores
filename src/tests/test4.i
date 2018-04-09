PROGRAM BigId BEGIN
    INT integer;
    FLOAT pfloat;
    STRING str := "isso e uma string";
    -- isso e um comentario

    FUNCTION FLOAT exp(INT a, INT b) BEGIN -- Faz a ^ b
        INT i;
        INT e; -- Como não da pra fazer “b+1” entro do for, eu coloquei no e
        FLOAT total;
        -- Como isso é um comentario o compila tem que ignorar
        -- RETURN 0;
        total := 1;
        e := b+1;
        IF ( b = 0 ) THEN
            RETURN 0;
        ELSE
            FOR(i := 1; i < e; i := i + 1)
                    total := total * a;
            ENDFOR
        ENDIF
    END,

    FUNCTION VOID helloWorld() BEGIN
        STRING hello := "Hello world";
        WRITE(hello);
    END,

    FUNCTION INT main () BEGIN
        INT i, qnt;
        STRING intro := "Por favor digite um numero: ";
        STRING barraN := "\n";

        WRITE(barraN);
        WRITE(intro);
        READ(qnt);
        FOR(i := 0; i < qnt; i := i + 1)
            WRITE(i, barraN);
        ENDFOR

    END,
END -- Acho que ta bom isso aqui, vê se faltou coisa
