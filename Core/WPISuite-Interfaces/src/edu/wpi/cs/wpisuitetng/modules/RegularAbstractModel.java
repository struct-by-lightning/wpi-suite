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

	public Boolean identify(CRTP curious) {
		return this.getID() == curious.getID();
	}
	
	public abstract String getID();
	public abstract void setID(String toSet);
	public abstract String getPrimaryKey();
}