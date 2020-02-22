package com.sinco.mybatis.dal.model;

/**
 * 唯一实体基类
 * 重写    equals 和 hashCode
 * @author Administrator
 *
 */
public abstract class UniqueModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5844631611956793937L;

	public UniqueModel(){
		
	}
	
	/**
	 * 返回代表该对象在该类型中唯一存在的code
	 * @return
	 */
	public abstract Object getUniqueCode(); 

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		// System.out.println(id!=((Resource)obj).getId());
		if (obj == null)
			return false;
		UniqueModel other = ((UniqueModel) obj);
		if (getUniqueCode() == null) {
			return false;
		}

		return getUniqueCode().equals(other.getUniqueCode());
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		int hash = 3;
		hash = 43 * hash + (this.getUniqueCode() != null ? this.getUniqueCode().hashCode() : 0);
		return hash;
	}
}
