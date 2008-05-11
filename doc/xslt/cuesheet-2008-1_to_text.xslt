<?xml version="1.0" encoding="UTF-8"?>
<!--
  Cuelib library for manipulating cue sheets.
  Copyright (C) 2007-2008 Jan-Willem van den Broek
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
-->
<!--
	This stylesheet can be used to convert the output of jwbroek.cuelib.CueSheetToXmlSerializer
	or any other xml document conforming to the schema for the http://jwbroek/cuelib/2008/cuesheet/1
	namespace into a corresponding cue sheet text file.
-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:cue="http://jwbroek/cuelib/2008/cuesheet/1">
	
	<!-- Cue sheets are not xml but normal text -->
	<xsl:output method="text"/>
	
	<!--
		This is a unix newline. Windows/DOS newlines are '&#xD;&#xA;', MAC OS-9 or before and Commodore
		newlines are '&#xA;'&#xD;.
	 -->
	<xsl:variable name="newline" select="'&#xA;'"/>
	<!-- 2 spaces are a single indentation level -->
	<xsl:variable name="indent" select="'  '"/>
	
	<xsl:template match="cue:cuesheet">
		<xsl:apply-templates select="@genre" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="@date" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="@discid" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="@comment" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="@catalog" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="@performer" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="@title" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="@songwriter" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="@cdtextfile" mode="cuesheet_attribute"/>
		<xsl:apply-templates select="cue:file"/>
	</xsl:template>
	
	<xsl:template match="@genre" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'REM GENRE '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@date" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'REM DATE '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@discid" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'REM DISCID '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@comment" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'REM COMMENT '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@catalog" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'CATALOG '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@performer" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'PERFORMER '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@title" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'TITLE '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@songwriter" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'SONGWRITER '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@cdtextfile" mode="cuesheet_attribute">
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'CDTEXTFILE '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="cue:file">
		<xsl:text>FILE </xsl:text>
		<xsl:call-template name="printValue">
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="@file"/>
		</xsl:call-template>
		<xsl:text> </xsl:text>
		<xsl:call-template name="printValue">
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="@type"/>
		</xsl:call-template>
		<xsl:value-of select="$newline"/>
		<xsl:apply-templates select="cue:track"/>
	</xsl:template>
	
	<xsl:template match="cue:track">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="1"/>
		</xsl:call-template>
		<xsl:text>TRACK </xsl:text>
		<xsl:value-of select="format-number(@number,'00')"/>
		<xsl:text> </xsl:text>
		<xsl:call-template name="printValue">
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="@type"/>
		</xsl:call-template>
		<xsl:value-of select="$newline"/>
		<xsl:apply-templates select="@isrc" mode="track_attribute"/>
		<xsl:apply-templates select="@performer" mode="track_attribute"/>
		<xsl:apply-templates select="@title" mode="track_attribute"/>
		<xsl:apply-templates select="@songwriter" mode="track_attribute"/>
		<xsl:apply-templates select="cue:pregap"/>
		<xsl:apply-templates select="cue:postgap"/>
		<xsl:apply-templates select="cue:flags"/>
		<xsl:apply-templates select="cue:index"/>
	</xsl:template>
	
	<xsl:template match="@isrc" mode="track_attribute">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="2"/>
		</xsl:call-template>
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'ISRC '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@performer" mode="track_attribute">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="2"/>
		</xsl:call-template>
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'PERFORMER '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@title" mode="track_attribute">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="2"/>
		</xsl:call-template>
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'TITLE '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="@songwriter" mode="track_attribute">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="2"/>
		</xsl:call-template>
		<xsl:call-template name="printLine">
			<xsl:with-param name="prefix" select="'SONGWRITER '"/>
			<xsl:with-param name="quote" select="'&quot;'"/>
			<xsl:with-param name="value" select="."/>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template match="cue:pregap">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="2"/>
		</xsl:call-template>
		<xsl:text>PREGAP </xsl:text>
		<xsl:call-template name="printPosition">
			<xsl:with-param name="positionElement" select="."/>
		</xsl:call-template>
		<xsl:value-of select="$newline"/>
	</xsl:template>
	
	<xsl:template match="cue:postgap">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="2"/>
		</xsl:call-template>
		<xsl:text>POSTGAP </xsl:text>
		<xsl:call-template name="printPosition">
			<xsl:with-param name="positionElement" select="."/>
		</xsl:call-template>
		<xsl:value-of select="$newline"/>
	</xsl:template>
	
	<xsl:template match="cue:flags">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="2"/>
		</xsl:call-template>
		<xsl:text>FLAGS</xsl:text>
		<xsl:for-each select="cue:flag">
			<xsl:text> </xsl:text>
			<xsl:call-template name="printValue">
				<xsl:with-param name="value" select="."/>
				<xsl:with-param name="quote" select="'&quot;'"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:value-of select="$newline"/>
	</xsl:template>
	
	<xsl:template match="cue:index">
		<xsl:call-template name="indent">
			<xsl:with-param name="level" select="2"/>
		</xsl:call-template>
		<xsl:text>INDEX </xsl:text>
		<xsl:value-of select="format-number(@number,'00')"/>
		<xsl:text> </xsl:text>
		<xsl:call-template name="printPosition">
			<xsl:with-param name="positionElement" select="."/>
		</xsl:call-template>
		<xsl:value-of select="$newline"/>
	</xsl:template>
	
	<!--
		Print a position of the form mm:ss:ff where mm corresponds to minutes, ss to seconds, and ff to frames.
		Each of these position elements must be in an attribute with the same name.
	-->
	<xsl:template name="printPosition">
		<!-- Element containing @minutes, @seconds, @frames-->
		<xsl:param name="positionElement"/>
		<xsl:value-of select="concat(format-number(@minutes,'00'),':',format-number(@seconds,'00'),':',format-number(@frames,'00'))"/>
	</xsl:template>
	
	<!-- Print a line containing some value that will be enclosed in quotes if it contain a space or tab. -->
	<xsl:template name="printLine">
		<!-- Start of the line. -->
		<xsl:param name="prefix"/>
		<!-- End of the line, excluding newline. -->
		<xsl:param name="suffix"/>
		<!-- Character used for quoting the value. Default is empty string, which effectively means no quotes. -->
		<xsl:param name="quote"/>
		<!-- Value to print on this line. May be empty. Will be enclosed in quotes if it contains a space or tab. -->
		<xsl:param name="value"/>
		
		<xsl:value-of select="$prefix"/>
		<xsl:if test="contains($value,' ') or contains($value,'	')">
			<xsl:value-of select="$quote"/>
		</xsl:if>
		<xsl:value-of select="$value"/>
		<xsl:if test="contains($value,' ') or contains($value,'	')">
			<xsl:value-of select="$quote"/>
		</xsl:if>
		<xsl:value-of select="$suffix"/>
		<xsl:value-of select="$newline"/>
	</xsl:template>
	
	<!-- Print a value that will be enclosed in quotes if it contains a space or tab. -->
	<xsl:template name="printValue">
		<!-- Character used for quoting the value. Default is empty string, which effectively means no quotes. -->
		<xsl:param name="quote"/>
		<!-- Value to print. May be empty. Will be enclosed in quotes if it contains a space or tab. -->
		<xsl:param name="value"/>
		
		<xsl:if test="contains($value,' ') or contains($value,'	')">
			<xsl:value-of select="$quote"/>
		</xsl:if>
		<xsl:value-of select="$value"/>
		<xsl:if test="contains($value,' ') or contains($value,'	')">
			<xsl:value-of select="$quote"/>
		</xsl:if>
	</xsl:template>
	
	<!-- Print the specified indentation level. -->
	<xsl:template name="indent">
		<!-- Level of indentation. Level 0 or below means no indentation. -->
		<xsl:param name="level"/>
		
		<xsl:if test="$level > 0">
			<xsl:value-of select="$indent"/>
			<xsl:call-template name="indent">
				<xsl:with-param name="level" select="$level - 1"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>