PROGRAM FunctionId Begin
    FUNCTION INT soma (INT c, INT d) BEGIN
        INT total;
        total := c + d;
        RETURN total;
    END,

    FUNCTION FLOAT avg (INT c, INT d) BEGIN
        FLOAT media;
        media := soma(c, d)/2;
        RETURN media;
    END,

    FUNCTION INT main () BEGIN  -- Aqui não sei se está certo
        INT a, b;
        FLOAT media;
        STRING init := "A media dos numeros ";
        STRING e := " e ";

        READ ( a, b );
        media := avg(a, b);
        WRITE(init, a, e, b, e, media); -- Aqui tbm não
        RETURN 0;
    END
END
