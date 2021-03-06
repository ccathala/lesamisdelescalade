<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<html>
<head>

<title>Site</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>


	<div class="container">

		<jsp:include page="header.jsp"></jsp:include>

		<h1>Rechercher un site</h1>
		<hr>
		<form:form action="searchSite" method="POST"
			modelAttribute="searchSiteData">

			<div class="form-group">
				<h3>Rechercher par nom:</h3>
				<form:input path="nom" class="col-lg-6 form-control"
					placeholder="Entrer le nom du site" />
			</div>


			<hr>
			<h3>Recherche multi-critères</h3>
			<div class="form-group">
				<label>Rechercher par département:</label>
				<form:select class="col-12 col-md-6 col-lg-4 form-control" path="departementId">
					<option value="0"
						<c:if test="${empty departementId }">selected</c:if>>Sélectionner
						le département</option>
					<c:forEach items="${departements }" var="departement">
						<option value="${departement.id }"
							<c:if test="${departementId == departement.id  }">selected</c:if>>${departement.code }-
							${departement.nom }</option>
					</c:forEach>
				</form:select>
			</div>

			<div class="form-group">
				<label>Rechercher par cotation:</label>
				<form:select class="col-12 col-sm-6 col-md-5 col-lg-4 form-control" path="cotationId">
					<option value="0" <c:if test="${empty cotationId }">selected</c:if>>Sélectionner
						la cotation</option>
					<c:forEach items="${cotations }" var="cotation">
						<option value=${cotation.id }
							<c:if test="${cotationId == cotation.id  }">selected</c:if>>${cotation.cotation }</option>
					</c:forEach>
				</form:select>
			</div>

			<div class="form-group">
				<label>Rechercher par nombre de secteurs:</label>
				<form:select class="col-12 col-md-6 col-lg-5 form-control" path="secteurCount">
					<option value="0"
						<c:if test="${empty secteurCount }">selected</c:if>>Sélectionner
						le nombre de secteurs</option>
					<c:forEach items="${secteurCountList }" var="count">
						<option value="${count}"
							<c:if test="${secteurCount == count  }">selected</c:if>>${count}</option>
					</c:forEach>
				</form:select>
			</div>
			<hr>
			
			<!-- Search Site button -->
			<div class="form-group">
				<div class="col-md-4">
					<button id="singlebutton" type="submit" name="singlebutton"
						class="btn btn-info">Rechercher</button>
				</div>
			</div>
		</form:form>
		
		<hr>

		<c:if test="${empty sites  }">
			<h2>Aucun site ne correspond aux critères saisis.</h2>
		</c:if>

		<c:forEach items="${sites }" var="site">
			<div class="card mb-3">
				<div class="row align-items-center no-gutters">
					<div class="col-md-4">
					<c:choose>
						<c:when test="${site.picture != null && !empty site.picture }">
						<img
							src="${site.picture }"
							class="card-img" alt="photo du site <c:out value="${site.nom }"></c:out>">
						</c:when>
						<c:when test="${site.picture == null || empty site.picture }">
						<img
							src="${pageContext.request.contextPath}/resources/pictures/generic_mountain.jpg"
							class="card-img" alt="image de montagne">
						</c:when>
					</c:choose>
						
					</div>
					<div class="col-md-8">
						<div class="card-body">
							<h3 class="card-title">
								<c:out value="${site.nom }"></c:out>
							</h3>
							
							<p class="card-text font-weight-bold">
								<c:out value="${departements_hashmap.get(site.departement_id).getCode() } - ${departements_hashmap.get(site.departement_id).getNom()}"></c:out>
							</p>
							
							<p class="card-text text-justify">
								<c:out value="${site.description }"></c:out>
							</p>
							<a class="btn btn-info"
								href="<c:url value="/site/${site.id }"></c:url>">En savoir
								plus </a>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>

		<jsp:include page="footer.jsp"></jsp:include>

	</div>

	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
		integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
		crossorigin="anonymous"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
		integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
		crossorigin="anonymous"></script>
	<script src="https://kit.fontawesome.com/60efee8a0b.js"
		crossorigin="anonymous"></script>
</body>
</html>