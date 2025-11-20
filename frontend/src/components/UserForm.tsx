import { useState, useEffect } from 'react';
import userService from '../services/userService';
import type { CreateUserRequest, UpdateUserRequest, ValidationError } from '../types/User';
import axios from 'axios';

interface UserFormProps {
  userId: number | null;
  onSuccess: (message: string) => void;
  onCancel: () => void;
}

function UserForm({ userId, onSuccess, onCancel }: UserFormProps) {
  const [formData, setFormData] = useState<CreateUserRequest | UpdateUserRequest>({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [generalError, setGeneralError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [loadingUser, setLoadingUser] = useState<boolean>(false);

  const isEditMode = userId !== null;

  useEffect(() => {
    if (isEditMode) {
      loadUser();
    }
  }, [userId]);

  const loadUser = async () => {
    if (!userId) return;

    try {
      setLoadingUser(true);
      const user = await userService.getUserById(userId);
      setFormData({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        phone: user.phone || '',
      });
    } catch (err) {
      setGeneralError('Failed to load user. Please try again.');
      console.error('Error loading user:', err);
    } finally {
      setLoadingUser(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    // Clear error for this field when user starts typing
    if (errors[name]) {
      setErrors((prev) => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.firstName.trim()) {
      newErrors.firstName = 'First name is required';
    } else if (formData.firstName.length > 50) {
      newErrors.firstName = 'First name must not exceed 50 characters';
    }

    if (!formData.lastName.trim()) {
      newErrors.lastName = 'Last name is required';
    } else if (formData.lastName.length > 50) {
      newErrors.lastName = 'Last name must not exceed 50 characters';
    }

    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Email must be a valid email address';
    } else if (formData.email.length > 100) {
      newErrors.email = 'Email must not exceed 100 characters';
    }

    if (formData.phone && formData.phone.length > 20) {
      newErrors.phone = 'Phone must not exceed 20 characters';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setGeneralError(null);

    if (!validateForm()) {
      return;
    }

    try {
      setLoading(true);
      if (isEditMode && userId) {
        await userService.updateUser(userId, formData);
        onSuccess('User updated successfully');
      } else {
        await userService.createUser(formData);
        onSuccess('User created successfully');
      }
    } catch (err) {
      if (axios.isAxiosError(err) && err.response) {
        const responseData = err.response.data as ValidationError | { message: string };
        
        if ('errors' in responseData) {
          // Validation errors from backend
          setErrors(responseData.errors);
        } else if ('message' in responseData) {
          // General error (e.g., duplicate email)
          setGeneralError(responseData.message);
        }
      } else {
        setGeneralError('An unexpected error occurred. Please try again.');
      }
      console.error('Error saving user:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loadingUser) {
    return <div className="loading">Loading user...</div>;
  }

  return (
    <div className="container">
      <h1>{isEditMode ? 'Edit User' : 'Add New User'}</h1>

      {generalError && (
        <div className="alert alert-error">
          {generalError}
        </div>
      )}

      <form onSubmit={handleSubmit} className="user-form">
        <div className="form-group">
          <label htmlFor="firstName">First Name *</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            className={errors.firstName ? 'error' : ''}
          />
          {errors.firstName && (
            <span className="error-message">{errors.firstName}</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="lastName">Last Name *</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            className={errors.lastName ? 'error' : ''}
          />
          {errors.lastName && (
            <span className="error-message">{errors.lastName}</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="email">Email *</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            className={errors.email ? 'error' : ''}
          />
          {errors.email && (
            <span className="error-message">{errors.email}</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="phone">Phone</label>
          <input
            type="text"
            id="phone"
            name="phone"
            value={formData.phone}
            onChange={handleChange}
            className={errors.phone ? 'error' : ''}
          />
          {errors.phone && (
            <span className="error-message">{errors.phone}</span>
          )}
        </div>

        <div className="form-actions">
          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Saving...' : isEditMode ? 'Update User' : 'Create User'}
          </button>
          <button type="button" onClick={onCancel} className="btn btn-secondary" disabled={loading}>
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
}

export default UserForm;
