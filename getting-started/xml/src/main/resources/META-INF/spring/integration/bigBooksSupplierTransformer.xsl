<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:sb="http://www.example.org/orders"
	xmlns:bb="http://www.example.org/orders-bigbooks">
	<xsl:template  match="/sb:orderItem" >
		<bb:bigBooksOrder>
			<bb:order>
				<bb:purchaser>smallbooks</bb:purchaser>
				<bb:quantity>5</bb:quantity>
				<bb:isbn>
					<xsl:value-of select="./sb:isbn/text()"/>
				</bb:isbn>
			</bb:order>
			
		</bb:bigBooksOrder>
	</xsl:template>
</xsl:stylesheet>