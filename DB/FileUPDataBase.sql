CREATE DATABASE IF NOT EXISTS `fileUPDataBase` DEFAULT CHARACTER SET latin1 COLLATE latin1_general_ci;

USE `fileUPDataBase`;

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `Usuario`;

CREATE TABLE `Usuario`
	
	(   `ID` 			nvarchar(20) 	NOT NULL
	,	`Nombre`		nvarchar(20)	NOT NULL
	,	`Apellidos`   	nvarchar(50)    DEFAULT NULL
	,	`Contraseña`	nvarchar(30)	NOT NULL

	,	PRIMARY KEY(`ID`)
	) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `Fichero`;

CREATE TABLE `Fichero`
	(	`ID_Fichero` 		nvarchar(20) 	NOT NULL
	,	`Nombre_Original` 	nvarchar(100)	NOT NULL
	,	`ID_Emisor` 		nvarchar(20)	NOT NULL
	,	`ID_Receptor`		nvarchar(20)	NOT NULL	
	,	`Ruta`				nvarchar(200)	NOT NULL
	,	`Codificacion`		nvarchar(20)	DEFAULT NULL

	,	PRIMARY KEY (`ID_Fichero`)
	,	CONSTRAINT `FK_emisor` FOREIGN KEY (`ID_Emisor`) REFERENCES `Usuario` (`ID`)
	,	CONSTRAINT `FK_receptor` FOREIGN KEY (`ID_Receptor`) REFERENCES `Usuario` (`ID`)
	)ENGINE=InnoDB DEFAULT CHARSET=latin1;
    
SET FOREIGN_KEY_CHECKS=1;