USE [master]
GO
/****** Object:  Database [Metal]    Script Date: 20/03/2019 16:39:03 ******/
CREATE DATABASE [Metal]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Metal', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\Metal.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Metal_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.SQLEXPRESS\MSSQL\DATA\Metal_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
GO
ALTER DATABASE [Metal] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Metal].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Metal] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Metal] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Metal] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Metal] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Metal] SET ARITHABORT OFF 
GO
ALTER DATABASE [Metal] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Metal] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Metal] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Metal] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Metal] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Metal] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Metal] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Metal] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Metal] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Metal] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Metal] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Metal] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Metal] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Metal] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Metal] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Metal] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Metal] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Metal] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [Metal] SET  MULTI_USER 
GO
ALTER DATABASE [Metal] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Metal] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Metal] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Metal] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Metal] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [Metal] SET QUERY_STORE = OFF
GO
USE [Metal]
GO
ALTER DATABASE SCOPED CONFIGURATION SET IDENTITY_CACHE = ON;
GO
ALTER DATABASE SCOPED CONFIGURATION SET LEGACY_CARDINALITY_ESTIMATION = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET LEGACY_CARDINALITY_ESTIMATION = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET MAXDOP = 0;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET MAXDOP = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET PARAMETER_SNIFFING = ON;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET PARAMETER_SNIFFING = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET QUERY_OPTIMIZER_HOTFIXES = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET QUERY_OPTIMIZER_HOTFIXES = PRIMARY;
GO
USE [Metal]
GO
/****** Object:  Table [dbo].[ArticulosFabricados]    Script Date: 20/03/2019 16:39:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ArticulosFabricados](
	[id_ArticuloFabricado] [int] IDENTITY(1,1) NOT NULL,
	[Descripcion] [varchar](50) NULL,
	[id_Familia] [int] NULL,
	[PrecioCoste] [float] NULL,
	[PrecioVenta] [float] NULL,
	[Stock] [int] NULL,
	[StockMinimo] [int] NULL,
	[Imagen] [varchar](max) NULL,
 CONSTRAINT [PK_ArticulosFabricados] PRIMARY KEY CLUSTERED 
(
	[id_ArticuloFabricado] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[clientes]    Script Date: 20/03/2019 16:39:03 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[clientes](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[Nombre] [varchar](50) NULL,
	[Direccion] [varchar](60) NULL,
	[Poblacion] [varchar](50) NULL,
	[Provincia] [varchar](30) NULL,
	[CodigoPostal] [varchar](5) NULL,
	[CifNif] [varchar](10) NULL,
	[Telefono1] [varchar](10) NULL,
	[Telefono2] [varchar](10) NULL,
	[Email] [varchar](50) NULL,
	[Web] [varchar](50) NULL,
	[PresonaContacto] [varchar](60) NULL,
	[SectorComercial] [varchar](80) NULL,
 CONSTRAINT [PK_clientes] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[copiaArticulosFabricados]    Script Date: 20/03/2019 16:39:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[copiaArticulosFabricados](
	[id_ArticuloFabricado] [varchar](50) NULL,
	[Descripcion] [varchar](50) NULL,
	[id_Familia] [varchar](50) NULL,
	[PrecioCoste] [varchar](50) NULL,
	[PrecioVenta] [varchar](50) NULL,
	[Stock] [varchar](50) NULL,
	[StockMinimo] [varchar](50) NULL,
	[Imagen] [varchar](50) NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DatosPorIdioma]    Script Date: 20/03/2019 16:39:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DatosPorIdioma](
	[id_Idioma] [int] NOT NULL,
	[id_ArticuloFabricado] [int] NOT NULL,
	[DatosTecnicos] [text] NULL,
	[id_Multimedia_DTecnicos] [int] NULL,
	[DatosMantenimiento] [text] NULL,
	[id_Multimedia_DMantenimiento] [int] NULL,
	[DatosMontaje] [ntext] NULL,
	[id_Multimedia_DMontaje] [int] NULL,
 CONSTRAINT [PK_DatosPorIdioma] PRIMARY KEY CLUSTERED 
(
	[id_Idioma] ASC,
	[id_ArticuloFabricado] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[EscandallosArticulosFabricados]    Script Date: 20/03/2019 16:39:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[EscandallosArticulosFabricados](
	[id_Escandallo] [int] IDENTITY(1,1) NOT NULL,
	[id_ArticuloFabricado] [int] NOT NULL,
	[id_MateriaPrima] [int] NOT NULL,
	[cantidad] [int] NOT NULL,
 CONSTRAINT [PK_EscandallosArticulosFabricados] PRIMARY KEY CLUSTERED 
(
	[id_Escandallo] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Idiomas]    Script Date: 20/03/2019 16:39:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Idiomas](
	[id_Idioma] [int] IDENTITY(1,1) NOT NULL,
	[Idioma] [varchar](50) NULL,
 CONSTRAINT [PK_Idiomas] PRIMARY KEY CLUSTERED 
(
	[id_Idioma] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LineasPresupuestos]    Script Date: 20/03/2019 16:39:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LineasPresupuestos](
	[id_LineaPresupuesto] [int] IDENTITY(1,1) NOT NULL,
	[id_Presupuesto] [int] NULL,
	[DescripcionLinea] [varchar](50) NULL,
	[id_Articulo] [int] NULL,
	[Precio] [float] NULL,
	[Cantidad] [int] NULL,
	[DatosTecnicos] [text] NULL,
	[DatosMantenimiento] [text] NULL,
	[DatosMontaje] [text] NULL,
	[Estado] [varchar](10) NULL,
 CONSTRAINT [PK_LineasPresupuestos] PRIMARY KEY CLUSTERED 
(
	[id_LineaPresupuesto] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MateriaPrimas]    Script Date: 20/03/2019 16:39:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MateriaPrimas](
	[id_MateriaPrima] [int] IDENTITY(1,1) NOT NULL,
	[Descripcion] [nchar](50) NULL,
	[id_Familia] [int] NULL,
	[PrecioCoste] [float] NULL,
	[Imagen] [nvarchar](max) NULL,
 CONSTRAINT [PK_MateriaPrima] PRIMARY KEY CLUSTERED 
(
	[id_MateriaPrima] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[MateriasPrimas]    Script Date: 20/03/2019 16:39:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[MateriasPrimas](
	[id_MateriaPrima] [int] NOT NULL,
	[Descripcion] [nvarchar](50) NOT NULL,
	[id_Familia] [int] NULL,
	[PrecioCoste] [float] NULL,
	[Imagen] [nvarchar](max) NULL,
 CONSTRAINT [PK_MateriaPrimas] PRIMARY KEY CLUSTERED 
(
	[id_MateriaPrima] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[multimedia]    Script Date: 20/03/2019 16:39:04 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[multimedia](
	[id_multimedia] [int] IDENTITY(1,1) NOT NULL,
	[descripcion] [varchar](60) NOT NULL,
	[tipo] [int] NOT NULL,
	[url] [varchar](120) NOT NULL,
 CONSTRAINT [PK_multimedia] PRIMARY KEY CLUSTERED 
(
	[id_multimedia] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Presupuestos]    Script Date: 20/03/2019 16:39:05 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Presupuestos](
	[id_Presupuesto] [int] IDENTITY(1,1) NOT NULL,
	[id_Cliente] [int] NOT NULL,
	[Fecha] [date] NOT NULL,
	[Descripcion] [varchar](max) NULL,
	[Estado] [varchar](10) NULL,
 CONSTRAINT [PK_Presupuestos] PRIMARY KEY CLUSTERED 
(
	[id_Presupuesto] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[ArticulosFabricados] ON 

INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (1, N'aaaaaaaaaaa', 1, 1, 2, 3, 4, N'J:\GestionMetal\Imagenes\animal2.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (2, N'Escavadora', 5, 247.34, 247.34, 0, 0, N'J:\GestionMetal\Imagenes\animal2.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (1002, N'Tornillo', 1, 2, 2, 2, 2, N'J:\GestionMetal\Imagenes\tornillo.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (1003, N'Tornillo2', 3, 3, 3, 3, 3, N'J:\GestionMetal\Imagenes\tornillo2.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (1004, N'Tornillo2', 3, 3, 3, 3, 3, N'J:\GestionMetal\Imagenes\tornillo2.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (1005, N'Taladro', 4, 4, 4, 4, 4, N'J:\GestionMetal\Imagenes\taladro.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (1006, N'Martillo', 5, 5, 5, 5, 5, N'j:\GestionMetal\Imagenes\martillo.JPG')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (1007, N'Rodamiento', 7, 7, 7, 7, 7, N'J:\GestionMetal\Imagenes\rodamiento.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (2002, N'Pepinillo', 6, 243, 243, 0, 0, N'Imagenes\icono.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (2003, N'Arandela', 8, 330, 330, 0, 0, N'Imagenes\icono.jpg')
INSERT [dbo].[ArticulosFabricados] ([id_ArticuloFabricado], [Descripcion], [id_Familia], [PrecioCoste], [PrecioVenta], [Stock], [StockMinimo], [Imagen]) VALUES (3002, N'Escalera', 4, 56517.188, 56517.188, 0, 0, N'J:\GestionMetal\Imagenes\LijadoraIndustrial.jpg')
SET IDENTITY_INSERT [dbo].[ArticulosFabricados] OFF
SET IDENTITY_INSERT [dbo].[clientes] ON 

INSERT [dbo].[clientes] ([id], [Nombre], [Direccion], [Poblacion], [Provincia], [CodigoPostal], [CifNif], [Telefono1], [Telefono2], [Email], [Web], [PresonaContacto], [SectorComercial]) VALUES (10, N'PericoPalotes', N'C/ la uerta', N'Aranda de Duero', N'Burgos', N'09400', N'12123456G', N'343', N'650', N'direccion@empres.com', N'www', N'Otro wew fffffffffffffff', N'Industrial')
INSERT [dbo].[clientes] ([id], [Nombre], [Direccion], [Poblacion], [Provincia], [CodigoPostal], [CifNif], [Telefono1], [Telefono2], [Email], [Web], [PresonaContacto], [SectorComercial]) VALUES (11, N'Epifanio Peludo', N'C/ de Allá', N'Aranda de Duero', N'Burgos', N'09400', N'33 33', N'2334', N'4353', N'345', N'www', N'yo', N'Madera')
INSERT [dbo].[clientes] ([id], [Nombre], [Direccion], [Poblacion], [Provincia], [CodigoPostal], [CifNif], [Telefono1], [Telefono2], [Email], [Web], [PresonaContacto], [SectorComercial]) VALUES (12, N'Pepe lopez', N'Amapolas 2', N'Aranda de Duero', N'Burgos', N'09400', N'4', N'3243245', N'345', N'345', N'www', N'tu', N'Construcción')
INSERT [dbo].[clientes] ([id], [Nombre], [Direccion], [Poblacion], [Provincia], [CodigoPostal], [CifNif], [Telefono1], [Telefono2], [Email], [Web], [PresonaContacto], [SectorComercial]) VALUES (13, N'Andres Estebanez', N'Corelacasa 13', N'Aranda de Duero', N'Burgos', N'09400', N'5', N'23423', N'345', N'345', N'www', N'el', N'Naval')
INSERT [dbo].[clientes] ([id], [Nombre], [Direccion], [Poblacion], [Provincia], [CodigoPostal], [CifNif], [Telefono1], [Telefono2], [Email], [Web], [PresonaContacto], [SectorComercial]) VALUES (1002, N'El otro', N'esquinita 11', N'Aranda de Duero', N'Burgos', N'09400', N'6', N'123', N'345', N'34535', N'www', N'nos', N'Limpieza')
SET IDENTITY_INSERT [dbo].[clientes] OFF
INSERT [dbo].[DatosPorIdioma] ([id_Idioma], [id_ArticuloFabricado], [DatosTecnicos], [id_Multimedia_DTecnicos], [DatosMantenimiento], [id_Multimedia_DMantenimiento], [DatosMontaje], [id_Multimedia_DMontaje]) VALUES (0, 2, N'ssssssssssss', 1, N'ssssssssssss', 1, N'fffffffffffffffffffffffff', 3)
INSERT [dbo].[DatosPorIdioma] ([id_Idioma], [id_ArticuloFabricado], [DatosTecnicos], [id_Multimedia_DTecnicos], [DatosMantenimiento], [id_Multimedia_DMantenimiento], [DatosMontaje], [id_Multimedia_DMontaje]) VALUES (1, 2, N'em frances', 1, N'em frances', 1, N'em frances', 3)
INSERT [dbo].[DatosPorIdioma] ([id_Idioma], [id_ArticuloFabricado], [DatosTecnicos], [id_Multimedia_DTecnicos], [DatosMantenimiento], [id_Multimedia_DMantenimiento], [DatosMontaje], [id_Multimedia_DMontaje]) VALUES (2, 1, N'ddddddddddddd', 1, N'fffffffffffffff', 2, N'rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr', 3)
INSERT [dbo].[DatosPorIdioma] ([id_Idioma], [id_ArticuloFabricado], [DatosTecnicos], [id_Multimedia_DTecnicos], [DatosMantenimiento], [id_Multimedia_DMantenimiento], [DatosMontaje], [id_Multimedia_DMontaje]) VALUES (2, 2, N'ddddddddddddddddddd', 1, N'fffffffffffffff', 2, N'nnnnnnnnnnnnnnnnnnnnnn', 3)
INSERT [dbo].[DatosPorIdioma] ([id_Idioma], [id_ArticuloFabricado], [DatosTecnicos], [id_Multimedia_DTecnicos], [DatosMantenimiento], [id_Multimedia_DMantenimiento], [DatosMontaje], [id_Multimedia_DMontaje]) VALUES (3, 2, N'Taladro', 1, N'Taladro', 2, N'Taladro', 3)
INSERT [dbo].[DatosPorIdioma] ([id_Idioma], [id_ArticuloFabricado], [DatosTecnicos], [id_Multimedia_DTecnicos], [DatosMantenimiento], [id_Multimedia_DMantenimiento], [DatosMontaje], [id_Multimedia_DMontaje]) VALUES (3, 2002, N'PortuguesnTecnocps', 1, N'Portuguesn maaaaaaaaaaaaaaaaaatenim', 1, N'Portuguesry moooooooooooooooooooooootaje', 3)
SET IDENTITY_INSERT [dbo].[EscandallosArticulosFabricados] ON 

INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (1, 2, 1, 1)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (2, 2, 2, 2)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (1002, 2002, 1, 1)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (1003, 2002, 2, 2)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (1004, 2002, 4, 3)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (1005, 2003, 2, 33)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (1006, 2003, 3, 33)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (2002, 3002, 82, 2)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (2003, 3002, 83, 4)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (2004, 3002, 1, 100)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (2005, 3002, 21, 200)
INSERT [dbo].[EscandallosArticulosFabricados] ([id_Escandallo], [id_ArticuloFabricado], [id_MateriaPrima], [cantidad]) VALUES (2006, 3002, 12, 457)
SET IDENTITY_INSERT [dbo].[EscandallosArticulosFabricados] OFF
SET IDENTITY_INSERT [dbo].[Idiomas] ON 

INSERT [dbo].[Idiomas] ([id_Idioma], [Idioma]) VALUES (1, N'Español')
INSERT [dbo].[Idiomas] ([id_Idioma], [Idioma]) VALUES (2, N'Frances')
INSERT [dbo].[Idiomas] ([id_Idioma], [Idioma]) VALUES (3, N'Aleman')
INSERT [dbo].[Idiomas] ([id_Idioma], [Idioma]) VALUES (4, N'Portugues')
SET IDENTITY_INSERT [dbo].[Idiomas] OFF
SET IDENTITY_INSERT [dbo].[LineasPresupuestos] ON 

INSERT [dbo].[LineasPresupuestos] ([id_LineaPresupuesto], [id_Presupuesto], [DescripcionLinea], [id_Articulo], [Precio], [Cantidad], [DatosTecnicos], [DatosMantenimiento], [DatosMontaje], [Estado]) VALUES (2084, 1022, N'Enganche', 1005, 4, 1, N'', N'', N'', N'Taller')
INSERT [dbo].[LineasPresupuestos] ([id_LineaPresupuesto], [id_Presupuesto], [DescripcionLinea], [id_Articulo], [Precio], [Cantidad], [DatosTecnicos], [DatosMantenimiento], [DatosMontaje], [Estado]) VALUES (2085, 1022, N'Cajon', 2, 247.34, 1, N'fffffffffffffffffffffffff', N'ssssssssssss', N'ssssssssssss', N'Fabricado')
INSERT [dbo].[LineasPresupuestos] ([id_LineaPresupuesto], [id_Presupuesto], [DescripcionLinea], [id_Articulo], [Precio], [Cantidad], [DatosTecnicos], [DatosMantenimiento], [DatosMontaje], [Estado]) VALUES (2086, 1022, N'Cajon', 1002, 2, 1, N'', N'', N'', N'Diseño')
INSERT [dbo].[LineasPresupuestos] ([id_LineaPresupuesto], [id_Presupuesto], [DescripcionLinea], [id_Articulo], [Precio], [Cantidad], [DatosTecnicos], [DatosMantenimiento], [DatosMontaje], [Estado]) VALUES (3011, 2011, N'Primera', 1002, 2, 1, N'', N'', N'', N'Fabricado')
SET IDENTITY_INSERT [dbo].[LineasPresupuestos] OFF
SET IDENTITY_INSERT [dbo].[MateriaPrimas] ON 

INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (1, N'Tornillo 10 X25                                   ', 1, 12323, N'j:\GestionMetal\Imagenes\tornillo.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (2, N'Tornillo cabeza 10 Y25                            ', 1, 2323, N'j:\GestionMetal\Imagenes\tornillo2.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (3, N'Arandela 12                                       ', 1, 0.45, N'j:\GestionMetal\Imagenes\arandela.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (4, N'Rodamiento Trasero EjeDereco                      ', 2, 0, N'j:\GestionMetal\Imagenes\rodamiento.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (5, N'Tornillo del 10                                   ', 1, 34, N'j:\GestionMetal\Imagenes\TornilloSimple.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (6, N'Rodamiento Doble V                                ', 2, 34, N'j:\GestionMetal\Imagenes\RDobleV.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (7, N'Rodamiento XL_U_L                                 ', 2, 34, N'j:\GestionMetal\Imagenes\rX_Y_L.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (8, N'Rosca Fina x12_24                                 ', 1, 34, N'j:\GestionMetal\Imagenes\tornilloFino.png')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (9, N'Electrodos soldadura Portatil                     ', 3, 343, N'j:\GestionMetal\Imagenes\electrodos.png')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (10, N'Electrodos soldadura Cordón Gruesol               ', 3, 0, N'j:\GestionMetal\Imagenes\electrodoGrueso.png')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (11, N'Sellador Silicona                                 ', 3, 0, N'j:\GestionMetal\Imagenes\SelladorSilicona.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (12, N'Electrodos Silicona de pistola                    ', 3, 0, N'j:\GestionMetal\Imagenes\siliconaPistola.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (13, N'Taladro                                           ', 4, 0, N'j:\GestionMetal\Imagenes\taladro.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (14, N'Martillo                                          ', 4, 0, N'j:\GestionMetal\Imagenes\martillo.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (15, N'Destornillador                                    ', 4, 235, N'j:\GestionMetal\Imagenes\destornillador.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (16, N'Maquina Soldadura Precision                       ', 5, 4556, N'j:\GestionMetal\Imagenes\soldaduraPrecision.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (17, N'Lijadora Industrial                               ', 5, 34545, N'j:\GestionMetal\Imagenes\LijadoraIndustrial.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (18, N'Chapa de acero modelo Abrahan                     ', 8, 345, N'j:\GestionMetal\Imagenes\chapaAceroAbrahan.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (19, N'Chapa Estandar Pulida                             ', 8, 4556, N'j:\GestionMetal\Imagenes\chapaLisa.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (20, N'Angulo Acero X1                                   ', 8, 34, N'j:\GestionMetal\Imagenes\AnguloAceroX1.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (21, N'Neumaticos Comb                                   ', 7, 545, N'j:\GestionMetal\Imagenes\ComboNeumaticos.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (22, N'Neumaticos Moto                                   ', 7, 0, N'j:\GestionMetal\Imagenes\neumaticosMoto.jpg')
INSERT [dbo].[MateriaPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (23, N'Neumatico Tractor Agricola                        ', 7, 4545, N'j:\GestionMetal\Imagenes\tractor.jpg')
SET IDENTITY_INSERT [dbo].[MateriaPrimas] OFF
INSERT [dbo].[MateriasPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (0, N'tres', 2, 23.45, N'J:\GestionMetal\Imagenes\taladro.jpg')
INSERT [dbo].[MateriasPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (1, N'Mariposa sa sa', 1, 1, N'J:\Basededatos\GestionMetal\Imagenes\animal0.jpg')
INSERT [dbo].[MateriasPrimas] ([id_MateriaPrima], [Descripcion], [id_Familia], [PrecioCoste], [Imagen]) VALUES (2, N'Oso', 2, 2, N'J:\Basededatos\GestionMetal\Imagenes\animal1.jpg')
SET IDENTITY_INSERT [dbo].[multimedia] ON 

INSERT [dbo].[multimedia] ([id_multimedia], [descripcion], [tipo], [url]) VALUES (1, N'Video Montaje maquina escavadora', 0, N'I:\MomentosMovil2018\IMG-20171218-WA0000.jpg')
INSERT [dbo].[multimedia] ([id_multimedia], [descripcion], [tipo], [url]) VALUES (2, N'qqqqqqqqqqq', 0, N'I:\MomentosMovil2018\IMG-20171216-WA0000.jpg')
SET IDENTITY_INSERT [dbo].[multimedia] OFF
SET IDENTITY_INSERT [dbo].[Presupuestos] ON 

INSERT [dbo].[Presupuestos] ([id_Presupuesto], [id_Cliente], [Fecha], [Descripcion], [Estado]) VALUES (1022, 12, CAST(N'2019-02-20' AS Date), N'El Presupuesto de pepe', N'Diseño')
INSERT [dbo].[Presupuestos] ([id_Presupuesto], [id_Cliente], [Fecha], [Descripcion], [Estado]) VALUES (2011, 11, CAST(N'2019-01-15' AS Date), N'Epipie', N'Taller')
SET IDENTITY_INSERT [dbo].[Presupuestos] OFF
USE [master]
GO
ALTER DATABASE [Metal] SET  READ_WRITE 
GO
