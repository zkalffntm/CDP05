class RecommandRouteWrapper implements Serializable {
	private List<RecommnadRoute> routeList = new ArrayList<>();

	class RecommandRoute implements Serializable  {
		private String description;
		private List<int> itemList = new ArrayList<>();
	}
}