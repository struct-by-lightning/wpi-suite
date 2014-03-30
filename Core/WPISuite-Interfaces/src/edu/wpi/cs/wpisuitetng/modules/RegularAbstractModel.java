package edu.wpi.cs.wpisuitetng.modules;

public abstract class RegularAbstractModel<CRTP extends RegularAbstractModel<CRTP>>
		extends AbstractModel {
	public void save() {
		return;
	}

	public void delete() {
		return;
	}

	public String toString() {
		return this.toJSON();
	}

	@SuppressWarnings("unchecked")
	public Boolean identify(Object o) {
		CRTP crtpo;
		try {
			crtpo = (CRTP) o;
		} catch(ClassCastException e) {
			return false;
		}
		return this.identify(crtpo);
	}

	public abstract Boolean identify(CRTP curious);
	public abstract String getID();
	public static String primaryKey;
}