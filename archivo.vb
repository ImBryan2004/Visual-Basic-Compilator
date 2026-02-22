Imports System
Imports System.IO

' ==============================
' PRUEBAS ANALIZADOR LEXICO
' ==============================

Module ProyectoPruebas

    Sub Main()

        ' -------------------------
        ' DECLARACIONES VALIDAS
        ' -------------------------

        Dim numero1 As Integer = 10
        Dim texto As String = "Hola"
        Dim activo As Boolean = True
        Dim byteVar As Byte = 5

        ' -------------------------
        ' ESPACIOS EXTRA (DEBEN SER VALIDOS)
        ' -------------------------

        Dim     numero2     As     Integer     =     20
        Dim texto2 As String="Texto sin espacios"
        Dim activo2    As Boolean=True

        ' -------------------------
        ' CONCATENACION VALIDA
        ' -------------------------

        Console.WriteLine("Resultado: " & numero1)
        Console.WriteLine(texto & " Mundo")
        Console.WriteLine("Suma: " & numero1 & numero2)

        ' -------------------------
        ' WRITE LINE CON ERRORES
        ' -------------------------

        Console.WriteLine()
        Console.WriteLine("Texto sin cerrar)
        Console.WriteLine("Texto" & )

        ' -------------------------
        ' TIPOS INCORRECTOS
        ' -------------------------

        Dim errorTipo1 As Integer = "Texto"
        Dim errorTipo2 As Boolean = 55
        Dim errorTipo3 As Byte = 5000

        ' -------------------------
        ' VARIABLES NO DECLARADAS
        ' -------------------------

        resultado = numero1 + numero2
        Console.WriteLine(resultado)

        ' -------------------------
        ' IDENTIFICADORES INVALIDOS
        ' -------------------------

        Dim 1numero As Integer = 5
        Dim _numero As Integer = 6
        Dim numero malo As Integer = 7

        ' -------------------------
        ' PALABRAS RESERVADAS COMO VARIABLES
        ' -------------------------

        Dim If As Integer = 5
        Dim Module As String = "Error"

        ' -------------------------
        ' OPERACIONES VALIDAS
        ' -------------------------

        Dim suma As Integer = numero1 + numero2
        Dim multi As Integer = numero1 * numero2

        ' -------------------------
        ' OPERACIONES INVALIDAS
        ' -------------------------

        Dim opError1 As Integer = numero1 + "hola"
        Dim opError2 As Integer = numero1 + desconocida

        ' -------------------------
        ' BOOLEAN VALIDOS
        ' -------------------------

        Dim bandera As Boolean = False
        Dim bandera2 As Boolean = True

        ' -------------------------
        ' BOOLEAN INVALIDOS
        ' -------------------------

        Dim banderaError As Boolean = numero1
        Dim banderaError2 As Boolean = "true"

        ' -------------------------
        ' LINEAS QUE NO DEBEN ANALIZARSE
        ' -------------------------

        If numero1 > 5 Then
            Console.WriteLine("Mayor")
        Else
            Console.WriteLine("Menor")
        End If

    End Sub

End Module